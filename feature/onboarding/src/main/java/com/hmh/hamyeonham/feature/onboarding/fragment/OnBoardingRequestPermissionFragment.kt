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
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentOnBoardingRequestPermissionBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingRequestPermissionFragment : Fragment() {
    private val binding by viewBinding(FragmentOnBoardingRequestPermissionBinding::bind)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentOnBoardingRequestPermissionBinding.inflate(inflater, container, false).root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        clickRequireAccessibilityButton()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.updateState {
            copy(isNextButtonActive = allPermissionIsGranted())
        }
        activityViewModel.sendEvent(OnboardEvent.changeActivityButtonText(getString(R.string.all_next)))
        activityViewModel.sendEvent(OnboardEvent.visibleProgressbar(true))
        setPermissionToggleState()
    }

    private fun clickRequireAccessibilityButton() {
        binding.run {
            clOnboardingUsageinfoPermission.setOnClickListener {
                if (hasUsageStatsPermission()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_usage_stats_permission))
                    tgOnboardingUsageinfoPermission.isChecked = true
                } else {
                    requestUsageAccessPermission()
                }
            }
            clOnboardingDrawoverPermission.setOnClickListener {
                if (hasOverlayPermission()) {
                    toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.already_overlay_permission))
                    tgOnboardingDrawoverPermission.isChecked = true
                } else {
                    requestOverlayPermission()
                }
            }
        }
    }

    private fun setPermissionToggleState() {
        binding.run {
            tgOnboardingUsageinfoPermission.isClickable = false
            tgOnboardingDrawoverPermission.isClickable = false
            tgOnboardingUsageinfoPermission.isChecked = hasUsageStatsPermission()
            tgOnboardingDrawoverPermission.isChecked = hasOverlayPermission()
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

    private fun hasOverlayPermission(): Boolean = context?.let { Settings.canDrawOverlays(it) } ?: false

    private fun allPermissionIsGranted(): Boolean = hasUsageStatsPermission() && hasOverlayPermission()
}
