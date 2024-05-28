package com.hmh.hamyeonham.core.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.lock.GetIsUnLockUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetUsageGoalsUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatFromPackageUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
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

    private var checkUsageJob: Job? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (getUsageIsLockUseCase()) return
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                checkUsageJob?.cancel()
                checkUsageJob = null
                checkUsageJob = monitorAndLockIfExceedsUsageGoal(event)
            }

            else -> Unit
        }
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
            val usageGoals = getUsageGoalsUseCase().firstOrNull() ?: return@launch
            val myGoal = usageGoals.find { it.packageName == packageName } ?: return@launch
            Log.d("LockAccessibilityService", "checkUsage: $usageStats")
            Log.d("LockAccessibilityService", "checkUsage: ${myGoal.goalTime}")
            if (usageStats > myGoal.goalTime) {
                val intent = navigationProvider.toLock(packageName).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
        }
    }

    override fun onInterrupt() {}
}

val lockAccessibilityServiceClassName: String =
    LockAccessibilityService::class.java.canonicalName.orEmpty()
