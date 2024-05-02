package com.hmh.hamyeonham.common.app

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import com.hmh.hamyeonham.common.R
import com.hmh.hamyeonham.common.context.toast

fun killAppByPackageName(context: Context, packageName: String) {
    Log.d("LockActivity", "killAppByPackageName: $packageName")
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    try {
        activityManager?.killBackgroundProcesses(packageName)
    } catch (e: Exception) {
        context.toast(context.getString(R.string.app_kill_fail))
        Log.e("LockActivity", "killAppByPackageName error : $e")
    }
}