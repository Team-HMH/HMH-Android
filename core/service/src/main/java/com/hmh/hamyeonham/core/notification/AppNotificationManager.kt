package com.hmh.hamyeonham.core.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = NotificationManagerCompat.from(context)
    private val notificationBuilder by lazy { ForegroundServiceNotificationBuilder(context) }

    fun setupNotificationChannel() {
        NotificationType.Push.entries.forEach {
            notificationManager.createNotificationChannel(
                NotificationChannelCompat.Builder(it.key, it.importance)
                    .setName(context.getString(it.notificationName))
                    .build()
            )
        }

        NotificationType.Local.entries.forEach {
            notificationManager.createNotificationChannel(
                NotificationChannelCompat.Builder(it.key, it.importance)
                    .setName(context.getString(it.notificationName))
                    .build()
            )
        }
    }

    fun createLockNotification(): Notification {
        return notificationBuilder.createLockNotification()
    }
}