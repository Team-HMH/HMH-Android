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
    private val selectedButtons = mutableSetOf<AppCompatButton>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentPeriodSelectionBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        initQuestionButton()
    }

    private fun initQuestionButton() {
        val periodSelectionFragmentButtonList = listOf(
            binding.btnNewChallengeSevenDays,
            binding.btnNewChallengeFourteenDays,
            binding.btnNewChallengeTwentyDays,
            binding.btnNewChallengeThirtyDays,
        )

        periodSelectionFragmentButtonList.forEachIndexed { _, button ->
            button.setOnClickListener {
                toggleButtonSelection(button)
            }
        }
    }

    private fun toggleButtonSelection(button: AppCompatButton) {
        button.isSelected = !button.isSelected
        updateSelectedButtons(button)
        updateUserResponse(button)
    }

    private fun updateSelectedButtons(newSelectedButton: AppCompatButton) {
        selectedButtons.filter { it != newSelectedButton }.forEach { it.isSelected = false }
        selectedButtons.clear()

        if (newSelectedButton.isSelected) {
            selectedButtons.add(newSelectedButton)
        } else {
            selectedButtons.remove(newSelectedButton)
        }
        viewModel.updateNextButtonActivatedState(selectedButtons.isNotEmpty())
    }

    private fun updateUserResponse(newSelectedButton: AppCompatButton) {
        if (newSelectedButton.isSelected) {
            val period = newSelectedButton.text.toString().extractDigits()
            viewModel.selectDate(period)
        } else {
            viewModel.unSelectDate()
        }
    }
}