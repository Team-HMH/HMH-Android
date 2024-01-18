package com.hmh.hamyeonham.feature.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hmh.hamyeonham.common.activity.checkAllPermissionIsGranted
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.dialog.TwoButtonCommonDialog
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.service.lockAccessibilityServiceClassName
import com.hmh.hamyeonham.core.viewmodel.MainViewModel
import com.hmh.hamyeonham.feature.main.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initNavHostFragment()
    }

    override fun onResume() {
        super.onResume()
        checkUnlockedPackage()
        checkAllPermissionIsGranted(lockAccessibilityServiceClassName)
    }

    private fun initNavHostFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fcvMain.id) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvMain.setupWithNavController(navController)
    }

    private fun checkUnlockedPackage() {
        val packageName = intent.getStringExtra(NavigationProvider.UN_LOCK_PACKAGE_NAME)
        if (packageName != null) {
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
                    intent.removeExtra(NavigationProvider.UN_LOCK_PACKAGE_NAME)
                }
                setDismissButtonClickListener {
                    intent.removeExtra(NavigationProvider.UN_LOCK_PACKAGE_NAME)
                }
            }.showAllowingStateLoss(supportFragmentManager, "unlock_package")
        }
    }
}
