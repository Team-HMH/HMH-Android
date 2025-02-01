package com.hmh.hamyeonham.firebase

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Application.setFirebaseCrashlyticsEnabled(isEnabled: Boolean) {

    fun getProcessName(): String? {
        val activityManager =
            applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val pid = android.os.Process.myPid()
        return activityManager.runningAppProcesses?.firstOrNull { it.pid == pid }?.processName
    }

    fun isMainProcess(): Boolean {
        return getProcessName() == applicationContext.packageName
    }

    if (isMainProcess()) {
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = isEnabled
    }
}