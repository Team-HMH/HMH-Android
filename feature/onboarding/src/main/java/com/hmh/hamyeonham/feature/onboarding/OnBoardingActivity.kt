package com.hmh.hamyeonham.feature.onboarding

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import com.hmh.hamyeonham.common.context.toast
import com.hmh.hamyeonham.common.view.initAndStartProgressBarAnimation
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.service.lockAccessibilityServiceClassName
import com.hmh.hamyeonham.feature.onboarding.adapter.OnBoardingFragmentStateAdapter
import com.hmh.hamyeonham.feature.onboarding.adapter.OnBoardingFragmentType
import com.hmh.hamyeonham.feature.onboarding.databinding.ActivityOnBoardingBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEffect
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ACCESS_TOKEN = "extra_access_token"
    }

    private val binding by viewBinding(ActivityOnBoardingBinding::inflate)
    private val viewModel by viewModels<OnBoardingViewModel>()
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var backButtonDelayTime: Long = 0

    private val accessibilitySettingsLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (checkAccessibilityServiceEnabled()) {
                toast(getString(com.hmh.hamyeonham.core.designsystem.R.string.success_accessibility_settings))
                navigateToNextOnboardingStep(binding.vpOnboardingContainer.adapter as OnBoardingFragmentStateAdapter)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        setBackPressedCallback()
        collectOnboardingState()
        collectSignUpEffect()
        changeOnBoardingButtonTextState()
        updateAccessToken()
        changeProgressbarVisibleState()
        updateBackButtonVisibility()
    }

    private fun updateBackButtonVisibility() {
        viewModel.onBoardingState
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.ivOnboardingBack.isVisible =
                    if (it.isBackButtonActive) true else false
            }.launchIn(lifecycleScope)
    }

    private fun updateAccessToken() {
        val accessToken = intent.getStringExtra(EXTRA_ACCESS_TOKEN)
        viewModel.sendEvent(OnboardEvent.UpdateAccessToken(accessToken.orEmpty()))
    }

    private fun collectSignUpEffect() {
        viewModel.onboardEffect
            .flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is OnboardEffect.OnboardSuccess -> {
                        moveToOnBoardingDoneSignUpActivity()
                    }

                    is OnboardEffect.OnboardFail -> {}
                }
            }.launchIn(lifecycleScope)
    }

    private fun initViews() {
        initBackButton()
        initViewPager()
    }

    private fun initBackButton() {
        binding.ivOnboardingBack.setOnClickListener {
            navigateToPreviousOnboardingStep()
        }
    }

    private fun initViewPager() {
        val pagerAdapter = setOnboardingPageAdapter()

        binding.btnOnboardingNext.setOnClickListener {
            viewModel.sendEvent(OnboardEvent.UpdateNextButtonActive(false))
            navigateToNextOnboardingStep(pagerAdapter)
        }

        viewModel.onBoardingState
            .flowWithLifecycle(lifecycle)
            .onEach {
                if (it.navigateToPermissionView &&
                    binding.vpOnboardingContainer.currentItem == OnBoardingFragmentType.SPECIFY_PERMISSION.position
                ) {
                    navigateToNextViewPager(
                        binding.vpOnboardingContainer,
                        OnBoardingFragmentType.SPECIFY_PERMISSION.position,
                    )
                }
            }.launchIn(lifecycleScope)
    }

    private fun navigateToPreviousOnboardingStep() {
        binding.vpOnboardingContainer.run {
            val currentItem = this.currentItem
            if (currentItem > 0) {
                this.currentItem = currentItem - 1
                updateProgressBar(currentItem - 1, adapter?.itemCount ?: 1)
            } else {
                onBackPressedCallback.isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun navigateToNextOnboardingStep(pagerAdapter: OnBoardingFragmentStateAdapter) {
        binding.vpOnboardingContainer.let { viewPager ->
            val currentItem = viewPager.currentItem
            val lastItem = pagerAdapter.itemCount - 1

            when {
                currentItem < lastItem -> {
                    if (currentItem == OnBoardingFragmentType.SPECIFY_PERMISSION.position) {
                        if (checkAccessibilityServiceEnabled()) {
                            navigateToNextViewPager(viewPager, currentItem)
                        } else {
                            requestAccessibilitySettings()
                        }
                    } else {
                        navigateToNextViewPager(viewPager, currentItem)
                    }
                }

                currentItem == lastItem -> {
                    viewModel.signUp()
                }

                else -> Unit
            }
        }
    }

    private fun navigateToNextViewPager(
        viewPager: ViewPager2,
        currentItem: Int,
    ) {
        viewPager.currentItem = currentItem + 1
        updateProgressBar(
            currentItem + 1,
            viewPager.adapter?.itemCount ?: 1,
        )
    }

    private fun requestAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        accessibilitySettingsLauncher.launch(intent)
    }

    private fun checkAccessibilityServiceEnabled(): Boolean {
        val service = "$packageName/$lockAccessibilityServiceClassName"
        val enabledServicesSetting =
            Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
            )
        return enabledServicesSetting?.contains(service) == true
    }

    private fun collectOnboardingState() {
        viewModel.onBoardingState
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.btnOnboardingNext.isEnabled = it.isNextButtonActive
            }.launchIn(lifecycleScope)
    }

    private fun changeOnBoardingButtonTextState() {
        viewModel.onBoardingState
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.btnOnboardingNext.text = it.buttonText
            }.launchIn(lifecycleScope)
    }

    private fun changeProgressbarVisibleState() {
        viewModel.onBoardingState
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.pbOnboarding.isVisible = it.progressbarVisible
            }.launchIn(lifecycleScope)
    }

    private fun setBackPressedCallback() {
        onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backButtonDelayTime >= 1000) {
                        backButtonDelayTime = currentTime
                        toast("‘뒤로’버튼을 한번 더 누르시면 종료됩니다.")
                    } else {
                        finish()
                    }
                }
            }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun moveToOnBoardingDoneSignUpActivity() {
        val intent =
            Intent(this, OnBoardingDoneSingUpActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        startActivity(intent)
        finish()
    }

    private fun setOnboardingPageAdapter(): OnBoardingFragmentStateAdapter {
        val pagerAdapter = OnBoardingFragmentStateAdapter(this)
        binding.vpOnboardingContainer.run {
            adapter = pagerAdapter
            isUserInputEnabled = false
            offscreenPageLimit = OFFSCREEN_PAGE_LIMIT_DEFAULT
        }
        return pagerAdapter
    }

    private fun updateProgressBar(
        currentItem: Int,
        totalItems: Int,
    ) {
        val progress = (currentItem + 1).toFloat() / totalItems.toFloat()
        val progressBarWidth = (progress * 100).toInt()
        binding.pbOnboarding.progress = progressBarWidth
        initAndStartProgressBarAnimation(binding.pbOnboarding, progressBarWidth)
    }

    override fun onDestroy() {
        onBackPressedCallback.remove()
        super.onDestroy()
    }
}
