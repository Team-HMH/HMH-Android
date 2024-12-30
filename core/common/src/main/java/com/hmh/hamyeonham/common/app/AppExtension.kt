package com.hmh.hamyeonham.common.app

import android.app.ActivityManager
import android.content.Context
import com.hmh.hamyeonham.common.R
import com.hmh.hamyeonham.common.context.toast
import timber.log.Timber

fun killAppByPackageName(context: Context, packageName: String) {

    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    try {
        activityManager?.killBackgroundProcesses(packageName)
        Timber.tag("LockActivity").d("killAppByPackageName: %s", packageName)
    } catch (e: Exception) {
        context.toast(context.getString(R.string.app_kill_fail))
        Timber.tag("LockActivity").e("killAppByPackageName error : %s", e.message)
    }
}