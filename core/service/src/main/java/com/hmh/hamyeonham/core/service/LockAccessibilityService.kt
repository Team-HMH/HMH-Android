package com.hmh.hamyeonham.core.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.IntentFilter
import android.view.accessibility.AccessibilityEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.core.date.DateChangedReceiver
import com.hmh.hamyeonham.core.lock.AppLockManger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var appLockManager: AppLockManger

    @Inject
    lateinit var dateChangedReceiver: DateChangedReceiver

    override fun onServiceConnected() {
        super.onServiceConnected()

        val filter = IntentFilter(Intent.ACTION_DATE_CHANGED)
        registerReceiver(dateChangedReceiver, filter)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            val packageName = event.packageName?.toString() ?: return@launch
            appLockManager.handleFocusedChangedEvent(packageName)
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        appLockManager.onDestroy()
        unregisterReceiver(dateChangedReceiver)
    }
}

val lockAccessibilityServiceClassName: String =
    LockAccessibilityService::class.java.canonicalName.orEmpty()