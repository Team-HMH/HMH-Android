package com.hmh.hamyeonham.challenge.newchallenge.periodselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.challenge.newchallenge.NewChallengeViewModel
import com.hmh.hamyeonham.common.primitive.extractDigits
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.challenge.databinding.FragmentPeriodSelectionBinding

class PeriodSelectionFragment : Fragment() {

    private val binding by viewBinding(FragmentPeriodSelectionBinding::bind)
    private val viewModel by activityViewModels<NewChallengeViewModel>()
    private lateinit var buttons: List<AppCompatButton>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentPeriodSelectionBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttons = listOf(
            binding.btnNewChallengeSevenDays,
            binding.btnNewChallengeFourteenDays,
            binding.btnNewChallengeTwentyDays,
            binding.btnNewChallengeThirtyDays,
        )
        initQuestionButton()
    }

    private fun initQuestionButton() {
        buttons.forEachIndexed { _, button ->
            button.setOnClickListener {
                initAllButtons(button)
            }
        }
    }

    private fun initAllButtons(button: AppCompatButton) {
        button.isSelected = !button.isSelected
        updateSelectedButtons(button)
    }

    private fun updateSelectedButtons(selectedButton: AppCompatButton) {
        if (selectedButton.isSelected) {
            val period = selectedButton.text.toString().extractDigits()
            viewModel.selectDate(period)
        } else {
            viewModel.unSelectDate()
        }
    }
}