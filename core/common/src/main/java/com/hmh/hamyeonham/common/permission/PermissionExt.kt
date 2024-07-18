package com.hmh.hamyeonham.common.permission

import android.Manifest
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hmh.hamyeonham.common.context.hasNotificationPermission

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
    onPermissionResult: PermissionRequestListener? = null
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val requestNotificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean ->
            onPermissionResult?.onPermissionGranted(Permission.NOTIFICATION)
        }
        when {
            hasNotificationPermission() -> {
                onPermissionResult?.onPermissionGranted(Permission.NOTIFICATION)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                showPermissionRationaleDialog {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            else -> {
                // 권한 요청
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    } else {
        onPermissionResult?.onPermissionGranted(Permission.NOTIFICATION)
    }
}

fun AppCompatActivity.allPermissionIsGranted(): Boolean {
    return hasNotificationPermission() && hasUsageStatsPermission() && hasOverlayPermission()
}


fun Fragment.requestOverlayPermission(listener: PermissionRequestListener? = null) {
    val packageUri = Uri.parse("package:${requireContext().packageName}")
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, packageUri)
    startActivity(intent)
    listener?.onPermissionDenied(Permission.OVERLAY) // 권한 요청 후에는 수동으로 결과 확인 필요
}

fun Fragment.requestUsageAccessPermission(listener: PermissionRequestListener? = null) {
    try {
        val packageUri = Uri.parse("package:${requireContext().packageName}")
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS, packageUri)
        startActivity(intent)
    } catch (e: Exception) {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }
    listener?.onPermissionDenied(Permission.USAGE_STATS) // 권한 요청 후에는 수동으로 결과 확인 필요
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
    onPermissionResult: PermissionRequestListener? = null
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val requestNotificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean ->
            onPermissionResult?.onPermissionGranted(Permission.NOTIFICATION)
        }
        when {
            hasNotificationPermission() -> {
                onPermissionResult?.onPermissionGranted(Permission.NOTIFICATION)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                showPermissionRationaleDialog {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            else -> {
                // 권한 요청
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    } else {
        onPermissionResult?.onPermissionGranted(Permission.NOTIFICATION)
    }
}

private fun AppCompatActivity.showPermissionRationaleDialog(onOk: () -> Unit) {
    AlertDialog.Builder(this)
        .setTitle("알림 권한 필요")
        .setMessage("이 앱은 정상적으로 작동하기 위해 알림 권한이 필요합니다. 권한을 허용해 주세요.")
        .setPositiveButton("확인") { _, _ -> onOk() }
        .setNegativeButton("취소", null)
        .show()
}

private fun AppCompatActivity.showPermissionDeniedDialog() {
    AlertDialog.Builder(this)
        .setTitle("알림 권한 필요")
        .setMessage("이 앱은 정상적으로 작동하기 위해 알림 권한이 필요합니다. 설정에서 권한을 허용해 주세요.")
        .setPositiveButton("설정 열기") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        }
        .setNegativeButton("취소", null)
        .show()
}

private fun Fragment.showPermissionRationaleDialog(onOk: () -> Unit) {
    AlertDialog.Builder(requireContext())
        .setTitle("알림 권한 필요")
        .setMessage("이 앱은 정상적으로 작동하기 위해 알림 권한이 필요합니다. 권한을 허용해 주세요.")
        .setPositiveButton("확인") { _, _ -> onOk() }
        .setNegativeButton("취소", null)
        .show()
}

private fun Fragment.showPermissionDeniedDialog() {
    AlertDialog.Builder(requireContext())
        .setTitle("알림 권한 필요")
        .setMessage("이 앱은 정상적으로 작동하기 위해 알림 권한이 필요합니다. 설정에서 권한을 허용해 주세요.")
        .setPositiveButton("설정 열기") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
            startActivity(intent)
        }
        .setNegativeButton("취소", null)
        .show()
}

fun Fragment.checkAllPermissionIsGranted(listener: PermissionRequestListener? = null) {
    when {
        !hasNotificationPermission() -> {
            requestNotificationPermission(onPermissionResult = listener)
        }

        !hasUsageStatsPermission() -> {
            requestUsageAccessPermission(listener = listener)
        }

        !hasOverlayPermission() -> {
            requestOverlayPermission(listener = listener)
        }
    }
}

enum class Permission {
    NOTIFICATION,
    USAGE_STATS,
    OVERLAY
}

interface PermissionRequestListener {
    fun onPermissionGranted(type: Permission)
    fun onPermissionDenied(type: Permission)
}

fun Fragment.allPermissionIsGranted(): Boolean {
    return hasNotificationPermission() && hasUsageStatsPermission() && hasOverlayPermission()
}
