package com.hmh.hamyeonham.core.lock

import android.content.Context
import android.content.Intent
import android.util.Log
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ServiceScoped
class AppLockManger @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getUsageStatFromPackageUseCase: GetUsageStatFromPackageUseCase,
    private val getUsageGoalsUseCase: GetUsageGoalsUseCase,
    private val getUsageIsLockUseCase: GetIsUnLockUseCase,
    private val navigationProvider: NavigationProvider,
    private val getTotalUsageStatsUseCase: GetTotalUsageStatsUseCase,
    private val getTotalUsageGoalUseCase: GetTotalUsageGoalUseCase,
) {
    private var checkUsageJob: Job? = null
    private var timerJob: Job? = null

    suspend fun handleFocusedChangedEvent(packageName: String) {
        if (getUsageIsLockUseCase()) return
        releaseCheckUsageJob()
        releaseTimerJob()
        checkUsageJob = monitorAndLockAppUsage(packageName)
    }

    private fun monitorAndLockAppUsage(packageName: String): Job {
        return CoroutineScope(Dispatchers.IO).launch {
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

    private suspend fun checkLockApp(
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

    private suspend fun moveToLock(packageName: String) {
        withContext(Dispatchers.Main) {
            val intent = navigationProvider.toLock(packageName).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    fun onDestroy() {
        releaseCheckUsageJob()
        releaseTimerJob()
    }
}
