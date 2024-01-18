package com.hmh.hamyeonham.common.fragment

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.snackBar(anchorView: View, message: () -> String) {
    Snackbar.make(anchorView, message(), Snackbar.LENGTH_SHORT).show()
}

fun Fragment.stringOf(@StringRes resId: Int, formatArgs: Any? = null) = getString(resId, formatArgs)

fun Fragment.colorOf(@ColorRes resId: Int) = ContextCompat.getColor(requireContext(), resId)

fun Fragment.drawableOf(@DrawableRes resId: Int) =
    ContextCompat.getDrawable(requireContext(), resId)

val Fragment.viewLifeCycle
    get() = viewLifecycleOwner.lifecycle

val Fragment.viewLifeCycleScope
    get() = viewLifecycleOwner.lifecycleScope

fun Fragment.requestAccessibilitySettings() {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    startActivity(intent)
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

fun Fragment.checkAccessibilityServiceEnabled(classCanonicalName: String): Boolean {
    val service = "${requireContext().packageName}/${classCanonicalName}"
    val enabledServicesSetting = Settings.Secure.getString(
        requireContext().contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
    )
    return enabledServicesSetting?.contains(service) == true
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

fun Fragment.checkAllPermissionIsGranted(classCanonicalName: String) {
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

fun Fragment.allPermissionIsGranted(classCanonicalName: String): Boolean {
    return this.checkAccessibilityServiceEnabled(classCanonicalName) && hasUsageStatsPermission() && hasOverlayPermission()
}
