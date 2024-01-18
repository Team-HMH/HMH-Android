package com.hmh.hamyeonham

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.databinding.ActivitySampleBinding
import com.hmh.hamyeonham.feature.login.LoginActivity
import com.hmh.hamyeonham.feature.onboarding.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySampleBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        initSplashAnimation(splashScreen)
        setContentView(binding.root)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun initSplashAnimation(splashScreen: SplashScreen) {
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val splashScreenView = splashScreenViewProvider.view
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            splashScreenView.startAnimation(fadeOut)
        }
    }
}
