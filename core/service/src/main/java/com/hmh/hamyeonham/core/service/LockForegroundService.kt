package com.hmh.hamyeonham.core.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.core.lock.AppLockManger
import com.hmh.hamyeonham.core.notification.AppNotificationManager
import com.hmh.hamyeonham.usagestats.repository.UsageStatsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// LockForegroundService.kt
@AndroidEntryPoint
class LockForegroundService : LifecycleService() {

    @Inject
    lateinit var notificationManager: AppNotificationManager

    @Inject
    lateinit var appLockManager: AppLockManger

    @Inject
    lateinit var usageStatsRepository: UsageStatsRepository

    override fun onCreate() {
        super.onCreate()
        isServiceRunning = true
        val notification = notificationManager.createLockNotification()
        startForeground(1, notification)
        monitorForegroundApp()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LockForegroundService", "onDestroy")
        appLockManager.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
        isServiceRunning = false
    }


    private fun monitorForegroundApp() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                val packageName = usageStatsRepository.getForegroundAppPackageName()
                if (packageName != null) {
                    Log.d("Usage", "packageName: $packageName")
                    appLockManager.handleFocusedChangedEvent(packageName)
                }
                delay(CHECK_INTERVAL)
            }
        }
    }

    companion object {
        private var isServiceRunning = false
        private const val CHECK_INTERVAL = 1000L
        fun startService(context: Context) {
            if (isServiceRunning) return
            val startIntent = Intent(context, LockForegroundService::class.java)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, LockForegroundService::class.java)
            context.stopService(stopIntent)
        }
    }
}