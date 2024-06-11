package com.hmh.hamyeonham.core.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.lock.GetIsUnLockUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetTotalUsageGoalUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetTotalUsageStatsUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetUsageGoalsUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatFromPackageUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var getUsageStatFromPackageUseCase: GetUsageStatFromPackageUseCase

    @Inject
    lateinit var getTotalUsageStatsUseCase: GetTotalUsageStatsUseCase

    @Inject
    lateinit var getUsageGoalsUseCase: GetUsageGoalsUseCase

    @Inject
    lateinit var getTotalUsageGoalUseCase: GetTotalUsageGoalUseCase

    @Inject
    lateinit var getUsageIsLockUseCase: GetIsUnLockUseCase

    @Inject
    lateinit var navigationProvider: NavigationProvider

    private var checkUsageJob: Job? = null
    private var timerJob: Job? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (getUsageIsLockUseCase()) return
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> resetCheckUsageJob(event)
            else -> Unit
        }
    }

    private fun resetCheckUsageJob(event: AccessibilityEvent) {
        releaseCheckUsageJob()
        checkUsageJob = monitorAndLockIfExceedsUsageGoal(event)
    }

    private fun monitorAndLockIfExceedsUsageGoal(event: AccessibilityEvent): Job {
        return ProcessLifecycleOwner.get().lifecycleScope.launch {
            val packageName = event.packageName?.toString() ?: return@launch

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
            Log.d("LockAccessibilityService", "checkUsage: $usageStats")
            Log.d("LockAccessibilityService", "checkUsage: ${myGoal.goalTime}")
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
        Log.d("lockAccessibilityService", "totalUsageStats:$totalUsageStats")
        Log.d("lockAccessibilityService", "totalUsageGoal:${totalUsageGoal.goalTime}")
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
    }
}

val lockAccessibilityServiceClassName: String =
    LockAccessibilityService::class.java.canonicalName.orEmpty()

fun main() {
    val lockAccessibilityServiceClassName =
        LockAccessibilityService::class.java.canonicalName.orEmpty()
    println(lockAccessibilityServiceClassName)
}