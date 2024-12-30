package com.hmh.hamyeonham.core.notification

import android.app.Notification
import android.content.Context
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

class ForegroundServiceNotificationBuilder(private val context: Context) {

    fun createLockNotification(): Notification {
        return NotificationCompat.Builder(context, NotificationType.Local.LOCK.key)
            .setContentTitle("하면함")
            .setContentText("하면함 앱 사용시간 측정 중")
            .setSmallIcon(com.hmh.hamyeonham.core.designsystem.R.drawable.hmh_app_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createRemoteViews(
        layoutId: Int,
    ): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, layoutId)
        return remoteViews
    }

}