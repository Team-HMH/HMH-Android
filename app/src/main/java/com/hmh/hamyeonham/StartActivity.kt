package com.hmh.hamyeonham

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.databinding.ActivitySampleBinding
import com.hmh.hamyeonham.feature.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySampleBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        initLottieSplash()
    }

    private fun initLottieSplash() {
        binding.splashLottieAppLogo.playAnimation()
        binding.splashLottieAppLogo.addAnimatorUpdateListener {
            if (it.animatedFraction == 1.0f) {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
