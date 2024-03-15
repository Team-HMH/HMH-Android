package com.hmh.hamyeonham.feature.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.databinding.ActivityOnBoardingStoryBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingStoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingStoryActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityOnBoardingStoryBinding::inflate)
    private val viewModel by viewModels<OnBoardingStoryViewModel>()

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeClickNum()
        storyClickListner()
    }

    private fun storyClickListner() {
        binding.root.setOnClickListener {
            viewModel.countStoryClickNum()
            when (viewModel.storyClickNum.value) {
                0 -> binding.ivStory.setImageResource(R.drawable.onboarding_story1)
                1 -> binding.ivStory.setImageResource(R.drawable.onboarding_story2)
                2 -> binding.ivStory.setImageResource(R.drawable.onboarding_story3)
                3 -> binding.ivStory.setImageResource(R.drawable.onboarding_story4)
                else -> navigateToMainActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(navigationProvider.toMain())
        finish()
    }

    private fun observeClickNum() {
        viewModel.storyClickNum.flowWithLifecycle(lifecycle).onEach {
        }.launchIn(lifecycleScope)
    }
}
