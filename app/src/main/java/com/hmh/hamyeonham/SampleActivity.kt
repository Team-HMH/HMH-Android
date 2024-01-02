package com.hmh.hamyeonham

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hmh.hamyeonham.feature.login.LoginActivity
import com.kakao.sdk.common.KakaoSdk
import com.hmh.hamyeonham.feature.main.MainActivity
import com.hmh.hamyeonham.feature.onboarding.OnBoardingActivity

class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        initSplashAnimation(splashScreen)

        setContentView(R.layout.activity_sample)

        initKakaoSdk()
        Intent(this, LoginActivity::class.java).let(::startActivity)
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        setContentView(R.layout.activity_sample)
        Intent(this, MainActivity::class.java).let(::startActivity)
    }

    private fun initSplashAnimation(splashScreen: SplashScreen) {
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val splashScreenView = splashScreenViewProvider.view
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    splashScreenViewProvider.remove()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            splashScreenView.startAnimation(fadeOut)
        }
    }
}
