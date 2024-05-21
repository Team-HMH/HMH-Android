package com.hmh.hamyeonham

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.databinding.ActivitySampleBinding
import com.hmh.hamyeonham.feature.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySampleBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        initLottieSplash()
    }

    private fun initLottieSplash() {
        binding.splashLottieAppLogo.playAnimation()
        val handler = Handler(
            Looper.getMainLooper()
        )
        handler.postDelayed(
            {
                navigateToLogin()
            }, 4000
        )
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
