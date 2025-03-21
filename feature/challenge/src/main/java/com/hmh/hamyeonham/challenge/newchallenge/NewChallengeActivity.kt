package com.hmh.hamyeonham.challenge.newchallenge

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.dialog.OneButtonCommonDialog
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.R
import com.hmh.hamyeonham.feature.challenge.databinding.ActivityNewChallengeBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject

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

            btNewChallenge.setOnClickListener {
                handleNextClicked()
                if (vpNewChallenge.adapter?.itemCount == FRAGMENT.PERIODSELECTION.position) {
                    val properties = JSONObject().put("period", viewModel.state.value.goalDate)
                    AmplitudeUtils.trackEventWithProperties(
                        "click_newchallenge_totaltime",
                        properties
                    )
                }
            }
            ivBack.setOnClickListener { finish() }
        }
    }

    private fun handleNextClicked() {
        binding.vpNewChallenge.run {
            when (currentItem) {
                FRAGMENT.PERIODSELECTION.position -> currentItem = FRAGMENT.TIMESELECTION.position
                FRAGMENT.TIMESELECTION.position -> showChallengeCreatedDialog()
            }
        }
    }

    private fun collectNewChallengeState() {
        viewModel.state.flowWithLifecycle(lifecycle).onEach {
            binding.btNewChallenge.isEnabled = it.isNextButtonActive
        }.launchIn(lifecycleScope)
    }

    private fun showChallengeCreatedDialog() {
        OneButtonCommonDialog.newInstance(
            title = getString(R.string.dialog_title_challengecreated),
            description = getString(R.string.dialog_description_challengecreated),
            iconRes = R.drawable.ic_challengecreated_120,
            confirmButtonText = getString(R.string.dialog_button_challengecreated),
            setBlueButton = true
        ).apply {
            setConfirmButtonClickListener {
                finishWithResults()
            }
        }.showAllowingStateLoss(supportFragmentManager, OneButtonCommonDialog.TAG)
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
