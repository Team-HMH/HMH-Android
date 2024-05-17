package com.hmh.hamyeonham.challenge.newchallenge

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.ActivityNewChallengeBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewChallengeActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNewChallengeBinding::inflate)
    private val viewModel by viewModels<NewChallengeViewModel>()

    companion object {
        const val PERIOD = "period"
        const val GOALTIME = "goal time"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        collectNewChallengeState()
    }

    private fun initViews() {
        binding.run {
            vpNewChallenge.adapter = NewChallengeViewPagerAdapter(this@NewChallengeActivity)
            vpNewChallenge.isUserInputEnabled = false

            btNewChallenge.setOnClickListener { handleNextClicked() }
            ivBack.setOnClickListener { finish() }
        }
    }

    private fun handleNextClicked() {
        binding.vpNewChallenge.run {
            when (currentItem) {
                0 -> currentItem = 1
                1 -> finishWithResults()
            }
        }
    }

    private fun collectNewChallengeState() {
        viewModel.state.flowWithLifecycle(lifecycle)
            .onEach { binding.btNewChallenge.isEnabled = it.isNextButtonActive }
            .launchIn(lifecycleScope)
    }

    private fun finishWithResults() {
        val intent = Intent().apply {
            putExtra(PERIOD, viewModel.state.value.goalDate)
            putExtra(GOALTIME, viewModel.state.value.goalTime)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}