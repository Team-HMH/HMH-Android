package com.hmh.hamyeonham.feature.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.databinding.ActivityOnBoardingDoneSingUpBinding
import com.hmh.hamyeonham.feature.onboarding.databinding.ActivityOnBoardingStoryBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingStoryActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityOnBoardingStoryBinding::inflate)

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_story)

        binding.root.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        startActivity(navigationProvider.toMain())
        finish()
    }
}