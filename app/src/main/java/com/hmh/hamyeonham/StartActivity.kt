package com.hmh.hamyeonham

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hmh.hamyeonham.common.dialog.OneButtonCommonDialog
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.databinding.ActivitySampleBinding
import com.hmh.hamyeonham.feature.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySampleBinding::inflate)
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        initLottieSplash()
    }

    private fun initLottieSplash() {
        binding.splashLottieAppLogo.playAnimation()
        OneButtonCommonDialog
            .newInstance(
                title = "서비스를 개선하고 있어요",
                description = "서비스를 개선하고 있어요 더 나은 모습으로 만나요",
                iconRes = null,
                confirmButtonText = "확인",
            )
            .setConfirmButtonClickListener {
                finish()
            }
            .showAllowingStateLoss(supportFragmentManager)

    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
