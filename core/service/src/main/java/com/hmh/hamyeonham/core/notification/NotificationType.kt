package com.hmh.hamyeonham.core.notification

import android.app.NotificationManager
import androidx.annotation.StringRes
import com.hmh.hamyeonham.core.service.R

sealed interface NotificationType {

    enum class Push(
        val key: String,
        @StringRes val notificationName: Int,
        val importance: Int,
    ) : NotificationType {

    }

    enum class Local(
        val key: String,
        @StringRes val notificationName: Int,
        val importance: Int,
    ) : NotificationType {
        LOCK("lock", R.string.app_notification_push_lock, NotificationManager.IMPORTANCE_HIGH)
    }


}