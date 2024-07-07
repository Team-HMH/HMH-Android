package com.hmh.hamyeonham.common.permission

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hmh.hamyeonham.common.context.toast
import com.hmh.hamyeonham.common.databinding.ActivityPermissionBinding
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPermissionBinding::inflate)

    @Inject
    lateinit var navigationProvider: NavigationProvider

    private val accessibilitySettingsLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (checkAccessibilityServiceEnabled()) {
                toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.success_accessibility_settings))
            }
        }

    private val overlayPermissionLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (hasOverlayPermission()) {
                toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.success_overlay_permission))
            }
        }

    private val usageStatsPermissionLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (hasUsageStatsPermission()) {
                toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.success_usage_stats_permission))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        if (allPermissionIsGranted()) {
            startActivity(navigationProvider.toMain())
            finish()
        }
        setPermissionToggleState()
    }

    private fun initViews() {
        binding.run {
            clAccessibilityPermission.setOnClickListener {
                if (checkAccessibilityServiceEnabled()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_accessibility_settings))
                    tgAccessibilityPermission.isChecked = true
                } else {
                    requestAccessibilitySettings()
                }
            }
            clUsageinfoPermission.setOnClickListener {
                if (hasUsageStatsPermission()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_usage_stats_permission))
                    tgUsageinfoPermission.isChecked = true
                } else {
                    requestUsageAccessPermission()
                }
            }
            clDrawoverPermission.setOnClickListener {
                if (hasOverlayPermission()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_overlay_permission))
                    tgDrawoverPermission.isChecked = true
                } else {
                    requestOverlayPermission()
                }
            }
        }
    }

    private fun setPermissionToggleState() {
        binding.run {
            tgAccessibilityPermission.isClickable = false
            tgUsageinfoPermission.isClickable = false
            tgDrawoverPermission.isClickable = false
            tgAccessibilityPermission.isChecked = checkAccessibilityServiceEnabled()
            tgUsageinfoPermission.isChecked = hasUsageStatsPermission()
            tgDrawoverPermission.isChecked = hasOverlayPermission()
        }
    }

    private fun requestAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        accessibilitySettingsLauncher.launch(intent)
    }

    private fun requestOverlayPermission() {
        val packageUri = Uri.parse("package:$packageName")
        overlayPermissionLauncher.launch(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                packageUri
            )
        )
    }

    private fun requestUsageAccessPermission() {
        try {
            val packageUri = Uri.parse("package:$packageName")
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS, packageUri)
            usageStatsPermissionLauncher.launch(intent)
        } catch (e: Exception) {
            usageStatsPermissionLauncher.launch(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    private fun checkAccessibilityServiceEnabled(): Boolean {
        val service = "$packageName/com.hmh.hamyeonham.core.service.LockAccessibilityService"
        val enabledServicesSetting = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
        )
        return enabledServicesSetting?.contains(service) == true
    }

    private fun hasUsageStatsPermission(): Boolean {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
        val time = System.currentTimeMillis()
        val stats = usageStatsManager?.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - 1000 * 60,
            time,
        )
        return !stats.isNullOrEmpty()

    }

    private fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    private fun allPermissionIsGranted(): Boolean {
        return checkAccessibilityServiceEnabled() && hasUsageStatsPermission() && hasOverlayPermission()
    }
}