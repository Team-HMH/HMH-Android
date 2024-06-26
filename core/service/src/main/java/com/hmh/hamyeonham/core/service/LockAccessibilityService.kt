package com.hmh.hamyeonham.core.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.IntentFilter
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
            val myEventType = event.eventType
            val myPackageName = event.packageName?.toString() ?: return@launch
            if (getUsageIsLockUseCase()) return@launch
            when (myEventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    handleFocusedChangedEvent(myPackageName)
                }

                else -> Unit
            }
            this.cancel()
        }
    }

    private fun handleFocusedChangedEvent(packageName: String) {
        releaseCheckUsageJob()
        releaseTimerJob()
        checkUsageJob = monitorAndLockIfExceedsUsageGoal(packageName)
    }

    private fun monitorAndLockIfExceedsUsageGoal(packageName: String): Job {
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
            val usageGoals = getUsageGoalsUseCase().firstOrNull() ?: return@launch
            val myGoal = usageGoals.find { it.packageName == packageName } ?: return@launch
            val totalGoal = getTotalUsageGoalUseCase()
            checkLockApp(usageStats, myGoal, packageName, totalUsageStats, totalGoal)
        }
    }

    private fun checkLockApp(
        usageStats: Long,
        myGoal: UsageGoal,
        packageName: String,
        totalUsageStats: Long,
        totalUsageGoal: UsageGoal
    ) {
        if (usageStats > myGoal.goalTime || totalUsageStats > totalUsageGoal.goalTime) {
            moveToLock(packageName)
        } else {
            releaseTimerJob()
            timerJob = ProcessLifecycleOwner.get().lifecycleScope.launch {
                val appRemainingTime = myGoal.goalTime - usageStats
                val totalRemainingTime = totalUsageGoal.goalTime - totalUsageStats
                val remainingTime =
                    if (appRemainingTime < totalRemainingTime) appRemainingTime else totalRemainingTime
                delay(remainingTime)
                moveToLock(packageName)
            }
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