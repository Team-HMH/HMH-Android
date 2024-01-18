package com.hmh.hamyeonham.feature.onboarding

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class OnBoardingAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName ?: return
        }
    }

    override fun onInterrupt() {}
}
