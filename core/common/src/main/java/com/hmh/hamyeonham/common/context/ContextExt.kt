package com.hmh.hamyeonham.common.context

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.hmh.hamyeonham.common.R
import timber.log.Timber

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.snackBar(
    anchorView: View,
    message: () -> String
) {
    Snackbar.make(anchorView, message(), Snackbar.LENGTH_SHORT).show()
}

fun Context.stringOf(
    @StringRes resId: Int
) = getString(resId)

fun Context.colorOf(
    @ColorRes resId: Int
) = ContextCompat.getColor(this, resId)

fun Context.drawableOf(
    @DrawableRes resId: Int
) = ContextCompat.getDrawable(this, resId)

fun Context.dialogWidthPercent(
    dialog: Dialog?,
    percent: Double = 0.8
) {
    val deviceSize = getDeviceSize()
    dialog?.window?.run {
        val params = attributes
        params.width = (deviceSize[0] * percent).toInt()
        attributes = params
    }
}

fun Context.getDeviceSize(): IntArray {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val windowInsets = windowMetrics.windowInsets

        val insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout()
        )
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom

        val bounds = windowMetrics.bounds

        return intArrayOf(bounds.width() - insetsWidth, bounds.height() - insetsHeight)
    } else {
        val display = windowManager.defaultDisplay
        val size = Point()

        display?.getSize(size)

        return intArrayOf(size.x, size.y)
    }
}

fun Context.getAppNameFromPackageName(packageName: String): String = try {
    val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    packageManager.getApplicationLabel(appInfo).toString()
} catch (e: PackageManager.NameNotFoundException) {
    "Unknown"
}

fun Context.getAppIconFromPackageName(packageName: String): Drawable? {
    try {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        return appInfo.loadIcon(packageManager)
    } catch (e: PackageManager.NameNotFoundException) {
        return ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground)
    }
}

fun Context.getSecondStrColoredString(
    firstStr: String,
    secondStr: String,
    color: Int
): SpannableStringBuilder {
    val mergedStr = "$firstStr $secondStr"
    val builder = SpannableStringBuilder(
        mergedStr
    )
    builder.setSpan(
        ForegroundColorSpan(
            ContextCompat.getColor(
                this,
                color
            )
        ),
        mergedStr.length - secondStr.length,
        mergedStr.length,
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
    )
    return builder
}

fun Context.isSystemPackage(packageName: String): Boolean {
    try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val applicationInfo = packageInfo.applicationInfo ?: throw Exception("applicationInfo null")
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.tag("isSystemPackage").e(e.toString())
    }
    return false
}

fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        // Android 13 이하 버전에서는 이 권한이 필요하지 않음
        true
    }
}