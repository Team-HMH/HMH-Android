package com.hmh.hamyeonham.feature.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.context.toast
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.login.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityLoginBinding::inflate)
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var loginViewPagerAdapter: LoginViewPagerAdapter

    @Inject
    lateinit var navigationProvider: NavigationProvider

    private lateinit var autoScrollJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ivKakaoLogin.setOnClickListener {
            viewModel.loginWithKakaoApp()
        }
        setLoginViewPager()
        handleKakaoLoginSuccess()
        handleAutoLoginSuccess()
    }

    private fun handleAutoLoginSuccess() {
        viewModel.loginState.flowWithLifecycle(lifecycle).onEach { state ->
            if (state.autoLogin) {
                navigateToMainActivity()
            }
        }.launchIn(lifecycleScope)
    }

    private fun handleKakaoLoginSuccess() {
        viewModel.kakaoLoginEvent.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is LoginEffect.LoginSuccess -> navigateToMainActivity()
                is LoginEffect.LoginFail -> toast(getString(R.string.fail_kakao_login))
                is LoginEffect.RequireSignUp -> navigateToOnBoardingActivity()
            }
        }.launchIn(lifecycleScope)
    }

    private fun setLoginViewPager() {
        val loginViewImageList = listOf(
            R.drawable.login_viewpager1,
            R.drawable.login_viewpager2,
            R.drawable.login_viewpager3,
        )

        loginViewPagerAdapter = LoginViewPagerAdapter(loginViewImageList)
        binding.vpLogin.adapter = loginViewPagerAdapter
        binding.indicatorLoginDots.attachTo(binding.vpLogin)
        startAutoScroll()
    }

    private fun startAutoScroll() {
        autoScrollJob = lifecycleScope.launch(Dispatchers.Main) {
            while (true) {
                delay(AUTO_SCROLL_DELAY)
                binding.vpLogin.setCurrentItem(
                    (binding.vpLogin.currentItem + 1) % loginViewPagerAdapter.itemCount,
                    false,
                )
            }
        }
    }

    private fun stopAutoScroll() {
        autoScrollJob.cancel()
    }

    private fun navigateToOnBoardingActivity() {
        val intent = navigationProvider.toOnBoarding()
        startActivity(intent)
        finish()
    }

    private fun navigateToMainActivity() {
        startActivity(navigationProvider.toMain())
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoScroll()
    }

    companion object {
        private const val AUTO_SCROLL_DELAY = 2000L
    }
}
