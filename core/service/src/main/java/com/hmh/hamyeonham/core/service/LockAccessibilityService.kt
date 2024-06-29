package com.hmh.hamyeonham.core.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.core.date.DateChangedReceiver
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.lock.GetIsUnLockUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetTotalUsageGoalUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetTotalUsageStatsUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetUsageGoalsUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatFromPackageUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var getUsageStatFromPackageUseCase: GetUsageStatFromPackageUseCase

    @Inject
    lateinit var getUsageGoalsUseCase: GetUsageGoalsUseCase

    @Inject
    lateinit var getUsageIsLockUseCase: GetIsUnLockUseCase

    @Inject
    lateinit var navigationProvider: NavigationProvider

    @Inject
    lateinit var getTotalUsageStatsUseCase: GetTotalUsageStatsUseCase

    @Inject
    lateinit var getTotalUsageGoalUseCase: GetTotalUsageGoalUseCase

    @Inject
    lateinit var dateChangedReceiver: DateChangedReceiver

    private var checkUsageJob: Job? = null
    private var timerJob: Job? = null

    override fun onServiceConnected() {
        super.onServiceConnected()

        val filter = IntentFilter(Intent.ACTION_DATE_CHANGED)
        registerReceiver(dateChangedReceiver, filter)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            val eventType = event.eventType
            val packageName = event.packageName?.toString() ?: return@launch
            if (getUsageIsLockUseCase()) return@launch
            if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                handleFocusedChangedEvent(packageName)
            }
            this.cancel()
        }
    }

    private fun handleFocusedChangedEvent(packageName: String) {
        releaseCheckUsageJob()
        releaseTimerJob()
        checkUsageJob = monitorAndLockAppUsage(packageName)
    }

    private fun monitorAndLockAppUsage(packageName: String): Job {
        return ProcessLifecycleOwner.get().lifecycleScope.launch {
            val (startTime, endTime) = getCurrentDayStartEndEpochMillis()
            val usageStats = getUsageStatFromPackageUseCase(
                startTime = startTime,
                endTime = endTime,
                packageName = packageName
            )
            val totalUsageStats = getTotalUsageStatsUseCase(
                startTime = startTime,
                endTime = endTime,
            )
            Log.d("Usage", "packageName: $packageName, usage: $usageStats")
            val usageGoals = getUsageGoalsUseCase().firstOrNull() ?: return@launch
            val totalUsageGoal = getTotalUsageGoalUseCase()
            val usageGoal = usageGoals.find {
                it.packageName == packageName
            } ?: return@launch
            checkLockApp(
                usageStats = usageStats,
                usageGoal = usageGoal,
                packageName = packageName,
                totalUsageStats = totalUsageStats,
                totalUsageGoal = totalUsageGoal
            )
        }
    }

    private fun checkLockApp(
        usageStats: Long,
        usageGoal: UsageGoal,
        packageName: String,
        totalUsageStats: Long,
        totalUsageGoal: UsageGoal
    ) {
        if (usageStats >= usageGoal.goalTime || totalUsageStats >= totalUsageGoal.goalTime) {
            moveToLock(packageName)
        } else {
            releaseTimerJob()
            val appRemainingTime = usageGoal.goalTime - usageStats
            val totalRemainingTime = totalUsageGoal.goalTime - totalUsageStats
            startTimer(
                appRemainingTime = appRemainingTime,
                totalRemainingTime = totalRemainingTime,
                packageName = packageName
            )
        }
    }

    private fun startTimer(
        appRemainingTime: Long,
        totalRemainingTime: Long,
        packageName: String
    ) {
        val remainingTime = minOf(appRemainingTime, totalRemainingTime)
        timerJob = ProcessLifecycleOwner.get().lifecycleScope.launch {
            delay(remainingTime)
            moveToLock(packageName)
        }
    }

    private fun releaseCheckUsageJob() {
        checkUsageJob?.cancel()
        checkUsageJob = null
    }

    private fun releaseTimerJob() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun moveToLock(packageName: String) {
        val intent = navigationProvider.toLock(packageName).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        releaseCheckUsageJob()
        releaseTimerJob()
        unregisterReceiver(dateChangedReceiver)
    }
}

val lockAccessibilityServiceClassName: String =
    LockAccessibilityService::class.java.canonicalName.orEmpty()