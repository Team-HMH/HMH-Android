package com.hmh.hamyeonham.feature.onboarding.fragment

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.common.fragment.toast
import com.hmh.hamyeonham.common.permission.hasNotificationPermission
import com.hmh.hamyeonham.common.permission.requestNotificationPermission
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentRequestPermissionBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestPermissionFragment : Fragment() {
    private val binding by viewBinding(FragmentRequestPermissionBinding::bind)
    private val activityViewModel by activityViewModels<OnBoardingViewModel>()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentRequestPermissionBinding.inflate(inflater, container, false).root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        clickRequireAccessibilityButton()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.sendEvent(OnboardEvent.UpdateNextButtonActive(allPermissionIsGranted()))
        activityViewModel.sendEvent(OnboardEvent.ChangeActivityButtonText(getString(R.string.all_next)))
        activityViewModel.sendEvent(OnboardEvent.VisibleProgressbar(true))
        setPermissionToggleState()
    }

    private fun clickRequireAccessibilityButton() {
        binding.run {
            clOnboardingUsageinfoPermission.setOnClickListener {
                if (hasUsageStatsPermission()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_permission_granted))
                    tgOnboardingUsageinfoPermission.isChecked = true
                } else {
                    requestUsageAccessPermission()
                }
            }
            clOnboardingDrawoverPermission.setOnClickListener {
                if (hasOverlayPermission()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_permission_granted))
                    tgOnboardingDrawoverPermission.isChecked = true
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
            tgOnboardingUsageinfoPermission.isClickable = false
            tgOnboardingDrawoverPermission.isClickable = false
            tgNotificationPermission.isClickable = false
            tgOnboardingUsageinfoPermission.isChecked = hasUsageStatsPermission()
            tgOnboardingDrawoverPermission.isChecked = hasOverlayPermission()
            tgNotificationPermission.isChecked = hasNotificationPermission()
        }
    }

    private fun requestOverlayPermission() {
        val packageUri = Uri.parse("package:" + requireContext().packageName)
        overlayPermissionLauncher.launch(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                packageUri,
            ),
        )
    }

    private fun requestUsageAccessPermission() {
        try {
            val packageUri = Uri.parse("package:" + requireContext().packageName)
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS, packageUri)
            usageStatsPermissionLauncher.launch(intent)
        } catch (e: Exception) {
            usageStatsPermissionLauncher.launch(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    private fun hasUsageStatsPermission(): Boolean {
        return context?.let {
            val usageStatsManager =
                it.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val stats =
                usageStatsManager.queryUsageStats(
                    UsageStatsManager.INTERVAL_DAILY,
                    time - 1000 * 60,
                    time,
                )
            return stats != null && stats.isNotEmpty()
        } ?: false
    }

    private fun hasOverlayPermission(): Boolean =
        context?.let { Settings.canDrawOverlays(it) } ?: false

    private fun allPermissionIsGranted(): Boolean =
        hasUsageStatsPermission() && hasOverlayPermission() && hasNotificationPermission()
}
