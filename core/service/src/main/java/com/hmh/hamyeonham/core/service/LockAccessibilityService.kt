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
import kotlinx.coroutines.Job
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

    private var checkUsageJob: Job? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED, AccessibilityEvent.TYPE_VIEW_CLICKED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                checkUsageJob?.cancel()
                checkUsageJob = null
                checkUsageJob = checkUsage(event)
            }

            else -> Unit
        }
    }

    private fun checkUsage(event: AccessibilityEvent): Job {
        val packageName = event.packageName?.toString() ?: return Job()

        val (startTime, endTime) = getCurrentDayStartEndEpochMillis()
        val usageStats = getUsageStatFromPackageUseCase(
            startTime = startTime,
            endTime = endTime,
            packageName = packageName
        )

        return ProcessLifecycleOwner.get().lifecycleScope.launch {
            // 이벤트 처리 로직
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
