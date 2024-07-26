package com.hmh.hamyeonham.common.permission

import android.Manifest
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hmh.hamyeonham.common.context.hasNotificationPermission
import com.hmh.hamyeonham.common.context.toast


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

fun AppCompatActivity.requestNotificationPermission(
    requestNotificationPermissionLauncher: ActivityResultLauncher<String>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        when {
            hasNotificationPermission() -> {
                toast("이미 알림 권한이 허용되었습니다.")
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                showPermissionDeniedDialog()
            }

            else -> {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

fun AppCompatActivity.allPermissionIsGranted(): Boolean {
    return hasNotificationPermission() && hasUsageStatsPermission() && hasOverlayPermission()
}


fun Fragment.requestOverlayPermission() {
    val packageUri = Uri.parse("package:${requireContext().packageName}")
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageUri)
    startActivity(intent)
}

fun Fragment.requestUsageAccessPermission() {
    try {
        val packageUri = Uri.parse("package:${requireContext().packageName}")
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS, packageUri)
        startActivity(intent)
    } catch (e: Exception) {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
}

fun Fragment.hasUsageStatsPermission(): Boolean {
    val usageStatsManager =
        requireContext().getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val time = System.currentTimeMillis()
    val stats = usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        time - 1000 * 60,
        time,
    )
    return stats != null && stats.isNotEmpty()
}

fun Fragment.hasOverlayPermission(): Boolean {
    return Settings.canDrawOverlays(requireContext())
}

fun Fragment.hasNotificationPermission(): Boolean {
    return requireContext().hasNotificationPermission()
}

fun Fragment.requestNotificationPermission(
    requestNotificationPermissionLauncher: ActivityResultLauncher<String>
) {
    (activity as? AppCompatActivity)?.requestNotificationPermission(
        requestNotificationPermissionLauncher
    )
}

private fun AppCompatActivity.showPermissionDeniedDialog() {
    AlertDialog.Builder(this)
        .setTitle("알림 권한 필요")
        .setMessage("이 앱은 정상적으로 작동하기 위해 알림 권한이 필요합니다. 설정에서 권한을 허용해 주세요.")
        .setPositiveButton("설정 열기") { _, _ ->
            val intent = Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
        .setNegativeButton("취소", null)
        .show()
}

fun Fragment.allPermissionIsGranted(): Boolean {
    return hasNotificationPermission() && hasUsageStatsPermission() && hasOverlayPermission()
}
