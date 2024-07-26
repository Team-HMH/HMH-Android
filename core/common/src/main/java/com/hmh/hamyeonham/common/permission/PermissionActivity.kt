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
import com.hmh.hamyeonham.common.context.hasNotificationPermission
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

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (hasNotificationPermission()) {
                toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.success_notification_permission))
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
            clUsageinfoPermission.setOnClickListener {
                if (hasUsageStatsPermission()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_permission_granted))
                    tgUsageinfoPermission.isChecked = true
                } else {
                    requestUsageAccessPermission()
                }
            }
            clDrawoverPermission.setOnClickListener {
                if (hasOverlayPermission()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_permission_granted))
                    tgDrawoverPermission.isChecked = true
                } else {
                    requestOverlayPermission()
                }
            }
            clNotificationPermission.setOnClickListener {
                if (hasNotificationPermission()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_permission_granted))
                } else {
                    requestNotificationPermission(requestNotificationPermissionLauncher)
                }
            }
        }
    }

    private fun setPermissionToggleState() {
        binding.run {
            tgUsageinfoPermission.isClickable = false
            tgDrawoverPermission.isClickable = false
            tgNotificationPermission.isClickable = false
            tgUsageinfoPermission.isChecked = hasUsageStatsPermission()
            tgDrawoverPermission.isChecked = hasOverlayPermission()
            tgNotificationPermission.isChecked = hasNotificationPermission()
        }
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
}