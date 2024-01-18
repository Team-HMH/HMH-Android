package com.hmh.hamyeonham.core.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.usagestats.usecase.GetUsageGoalsUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatFromPackageUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var getUsageStatFromPackageUseCase: GetUsageStatFromPackageUseCase

    @Inject
    lateinit var getUsageGoalsUseCase: GetUsageGoalsUseCase

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            checkUsage(event)
        }
    }

    private fun checkUsage(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString() ?: return
        val (startTime, endTime) = getCurrentDayStartEndEpochMillis()
        val usageStats = getUsageStatFromPackageUseCase(
            startTime = startTime,
            endTime = endTime,
            packageName = packageName
        )

        ProcessLifecycleOwner.get().lifecycleScope.launch {
            val usageGoals = getUsageGoalsUseCase().first()
            val myGoal = usageGoals.find { it.packageName == packageName } ?: return@launch
            if (usageStats > myGoal.goalTime) {
                navigationProvider.toLock(packageName).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.let(::startActivity)
            }

        }
    }

    override fun onInterrupt() {}
}

val lockAccessibilityServiceClassName: String =
    LockAccessibilityService::class.java.canonicalName.orEmpty()
