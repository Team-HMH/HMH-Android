package com.hmh.hamyeonham.feature.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.context.hasNotificationPermission
import com.hmh.hamyeonham.common.dialog.OneButtonCommonDialog
import com.hmh.hamyeonham.common.dialog.TwoButtonCommonDialog
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.permission.Permission
import com.hmh.hamyeonham.common.permission.PermissionRequestListener
import com.hmh.hamyeonham.common.permission.allPermissionIsGranted
import com.hmh.hamyeonham.common.permission.isBatteryOptimizationEnabled
import com.hmh.hamyeonham.common.permission.requestDisableBatteryOptimization
import com.hmh.hamyeonham.common.permission.requestNotificationPermission
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.service.LockForegroundService
import com.hmh.hamyeonham.core.viewmodel.MainEffect
import com.hmh.hamyeonham.core.viewmodel.MainViewModel
import com.hmh.hamyeonham.feature.main.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initNavHostFragment()
        checkPermission()
        collectEffect()
    }

    override fun onResume() {
        super.onResume()
        checkUnlockedPackage()
    }

    private fun collectEffect() {
        viewModel.effect.flowWithLifecycle(lifecycle).onEach { effect ->
            when (effect) {
                is MainEffect.SuccessUsePoint -> {
                    intent.removeExtra(NavigationProvider.UN_LOCK_PACKAGE_NAME)
                    showChallengeFailedDialog()
                }

                is MainEffect.LackOfPoint -> showPointLackDialog()
                is MainEffect.NetworkError -> showErrorDialog()
            }
        }.launchIn(lifecycleScope)
    }

    private fun initNavHostFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fcvMain.id) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvMain.setupWithNavController(navController)
    }

    private fun checkUnlockedPackage() {
        val packageName = intent.getStringExtra(NavigationProvider.UN_LOCK_PACKAGE_NAME) ?: return
        TwoButtonCommonDialog.newInstance(
            title = getString(
                R.string.dialog_title_unlock_package,
                getAppNameFromPackageName(packageName)
            ),
            description = getString(R.string.dialog_description_unlock_package),
            confirmButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.all_okay),
            dismissButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.all_cancel),
        ).apply {
            setConfirmButtonClickListener {
                viewModel.updateDailyChallengeFailed()
            }
            setDismissButtonClickListener {
                intent.removeExtra(NavigationProvider.UN_LOCK_PACKAGE_NAME)
            }
        }.showAllowingStateLoss(supportFragmentManager, "unlock_package")
    }

    private fun showChallengeFailedDialog() {
        OneButtonCommonDialog.newInstance(
            title = getString(R.string.dialog_title_challenge_failed),
            description = getString(R.string.dialog_description_challenge_failed),
            iconRes = R.drawable.ic_challenge_failed,
            confirmButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.all_okay),
        ).apply {
            setConfirmButtonClickListener {
                dismiss()
            }
        }.showAllowingStateLoss(supportFragmentManager, OneButtonCommonDialog.TAG)
    }

    private fun showPointLackDialog() {
        OneButtonCommonDialog.newInstance(
            title = getString(R.string.dialog_title_point_lack),
            description = getString(R.string.dialog_description_point_lack),
            iconRes = R.drawable.ic_point_lack,
            confirmButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.no),
        ).apply {
            setConfirmButtonClickListener {
                dismiss()
            }
        }.showAllowingStateLoss(supportFragmentManager, OneButtonCommonDialog.TAG)
    }

    private fun showErrorDialog() {
        OneButtonCommonDialog.newInstance(
            title = getString(com.hmh.hamyeonham.core.designsystem.R.string.dialog_title_network_error),
            description = getString(com.hmh.hamyeonham.core.designsystem.R.string.dialog_description_network_error),
            confirmButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.all_okay),
        ).apply {
            setConfirmButtonClickListener {
                dismiss()
            }
        }.showAllowingStateLoss(supportFragmentManager, OneButtonCommonDialog.TAG)
    }

    private fun checkPermission() {
        if (allPermissionIsGranted()) {
            // TODO Permission 화면 보내기
        }

        checkPowerManagerPermission()
    }

    private fun checkPowerManagerPermission() {
        if (isBatteryOptimizationEnabled()) {
            requestDisableBatteryOptimization()
        }
    }
}
