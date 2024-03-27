package com.hmh.hamyeonham.feature.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.databinding.ActivityOnBoardingDoneSingUpBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingDoneSingUpActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityOnBoardingDoneSingUpBinding::inflate)

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnOnboardingDoneNext.setOnClickListener {
            navigateToMainStory()
        }
    }

    private fun navigateToMainStory() {
        startActivity(navigationProvider.toOnBoardingStory())
        finish()
    }
}
