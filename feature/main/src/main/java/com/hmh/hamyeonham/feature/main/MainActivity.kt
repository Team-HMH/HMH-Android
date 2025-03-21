package com.hmh.hamyeonham.feature.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.context.hasNotificationPermission
import com.hmh.hamyeonham.common.dialog.OneButtonCommonDialog
import com.hmh.hamyeonham.common.dialog.TwoButtonCommonDialog
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.permission.allPermissionIsGranted
import com.hmh.hamyeonham.common.permission.isBatteryOptimizationEnabled
import com.hmh.hamyeonham.common.permission.requestDisableBatteryOptimization
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.service.LockForegroundService
import com.hmh.hamyeonham.core.viewmodel.MainEffect
import com.hmh.hamyeonham.core.viewmodel.MainViewModel
import com.hmh.hamyeonham.feature.main.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        collectEffect()
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
        startLockService()
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

    private fun initViews() {
        initViewPager()
        initBottomNavigation()
    }

    private fun initViewPager() {
        binding.vpMain.apply {
            isUserInputEnabled = false
            adapter = MainAdapter(this@MainActivity)
            setCurrentItem(MainScreen.HOME.ordinal, false)

        }
    }

    private fun initBottomNavigation() {
        binding.bnvMain.selectedItemId = R.id.home_dest
        binding.bnvMain.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.challenge_dest -> binding.vpMain.setCurrentItem(
                    MainScreen.CHALLENGE.ordinal,
                    false
                )

                R.id.home_dest -> binding.vpMain.setCurrentItem(
                    MainScreen.HOME.ordinal,
                    false
                )

                R.id.my_page_dest -> binding.vpMain.setCurrentItem(
                    MainScreen.MY_PAGE.ordinal,
                    false
                )
            }
            true
        }
    }

    private fun checkUnlockedPackage() {
        val packageName = intent.getStringExtra(NavigationProvider.UN_LOCK_PACKAGE_NAME) ?: return
        TwoButtonCommonDialog.newInstance(
            title =
            getString(
                R.string.dialog_title_unlock_package,
                getAppNameFromPackageName(packageName),
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

    private fun startLockService() {
        if (hasNotificationPermission()) {
            LockForegroundService.startService(this)
        }
    }

    private fun showChallengeFailedDialog() {
        OneButtonCommonDialog.newInstance(
            title = getString(R.string.dialog_title_challenge_failed),
            description = getString(R.string.dialog_description_challenge_failed),
            iconRes = R.drawable.ic_challenge_failed,
            confirmButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.all_done),
        ).apply {
            setConfirmButtonClickListener {
                AmplitudeUtils.trackEventWithProperties("click_unlock_complete_button")
                dismiss()
            }
        }.showAllowingStateLoss(supportFragmentManager, OneButtonCommonDialog.TAG)
    }

    private fun showPointLackDialog() {
        TwoButtonCommonDialog.newInstance(
            title = getString(R.string.dialog_title_point_lack),
            description = getString(R.string.dialog_description_point_lack),
            iconRes = R.drawable.ic_point_lack,
            confirmButtonText = getString(com.hmh.hamyeonham.core.designsystem.R.string.all_okay),
            dismissButtonText = getString(R.string.dialog_button_charge_point)
        ).apply {
            setConfirmButtonClickListener {
                dismiss()
            }
            setDismissButtonClickListener {
                val intent = navigationProvider.toStore()
                startActivity(intent)
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
        if (!allPermissionIsGranted()) {
            startActivity(navigationProvider.toPermission())
        }
        checkPowerManagerPermission()
    }

    private fun checkPowerManagerPermission() {
        if (isBatteryOptimizationEnabled()) {
            requestDisableBatteryOptimization()
        }
    }
}
