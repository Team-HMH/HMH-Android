package com.hmh.hamyeonham.common.activity

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

fun AppCompatActivity.replaceFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.commit {
        replace(containerViewId, fragment)
    }
}

fun AppCompatActivity.addFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.commit {
        add(containerViewId, fragment)
    }
}

fun AppCompatActivity.requestAccessibilitySettings() {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    startActivity(intent)
}

fun AppCompatActivity.requestOverlayPermission() {
    val packageUri = Uri.parse("package:$packageName")
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageUri)
    startActivity(intent)
}

fun AppCompatActivity.requestUsageAccessPermission() {
    try {
        val packageUri = Uri.parse("package:$packageName")
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS, packageUri)
        startActivity(intent)
    } catch (e: Exception) {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}

fun AppCompatActivity.checkAccessibilityServiceEnabled(classCanonicalName: String): Boolean {
    val service = "$packageName/$classCanonicalName"
    val enabledServicesSetting = Settings.Secure.getString(
        contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
    )
    return enabledServicesSetting?.contains(service) == true
}

fun AppCompatActivity.hasUsageStatsPermission(): Boolean {
    val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val time = System.currentTimeMillis()
    val stats = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        time - 1000 * 60,
        time,
    )
    return stats != null && stats.isNotEmpty()
}

fun AppCompatActivity.hasOverlayPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}

fun AppCompatActivity.checkAllPermissionIsGranted(classCanonicalName: String) {
    when {
        !checkAccessibilityServiceEnabled(classCanonicalName) -> {
            requestAccessibilitySettings()
        }

        !hasUsageStatsPermission() -> {
            requestUsageAccessPermission()
        }

        !hasOverlayPermission() -> {
            requestOverlayPermission()
        }
    }
}

fun AppCompatActivity.allPermissionIsGranted(classCanonicalName: String): Boolean {
    return checkAccessibilityServiceEnabled(classCanonicalName) && hasUsageStatsPermission() && hasOverlayPermission()
}

fun AppCompatActivity.isBatteryOptimizationEnabled(): Boolean {
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    val packageName = packageName
    return !powerManager.isIgnoringBatteryOptimizations(packageName)
}

fun AppCompatActivity.requestDisableBatteryOptimization() {
    if (isBatteryOptimizationEnabled()) {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = Uri.parse("package:$packageName")
        }
        startActivity(intent)
    }
}

