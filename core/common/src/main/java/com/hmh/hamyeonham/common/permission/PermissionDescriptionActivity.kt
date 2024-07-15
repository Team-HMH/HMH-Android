package com.hmh.hamyeonham.common.permission

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hmh.hamyeonham.common.activity.checkAccessibilityServiceEnabled
import com.hmh.hamyeonham.common.context.toast
import com.hmh.hamyeonham.common.databinding.ActivityPermissionDescriptionBinding
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.designsystem.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionDescriptionActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPermissionDescriptionBinding::inflate)

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onResume() {
        super.onResume()
        checkAccessibilityAndNavigateToPermission()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setButtonClickListener()
    }

    private fun setButtonClickListener() {
        binding.btnNext.setOnClickListener {
            requestAccessibilitySettings()
        }
        binding.ivBack.setOnClickListener { finish() }
    }

    private val accessibilitySettingsLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            checkAccessibilityAndNavigateToPermission()
        }

    private fun checkAccessibilityAndNavigateToPermission() {
        if (checkAccessibilityServiceEnabled()) {
            toast(getString(R.string.success_accessibility_settings))
            startActivity(navigationProvider.toPermission())
            finish()
        }
    }

    private fun requestAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        accessibilitySettingsLauncher.launch(intent)
    }

    private fun checkAccessibilityServiceEnabled(): Boolean {
        val service = "$packageName/com.hmh.hamyeonham.core.service.LockAccessibilityService"
        val enabledServicesSetting = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
        )
        return enabledServicesSetting?.contains(service) == true
    }
}
