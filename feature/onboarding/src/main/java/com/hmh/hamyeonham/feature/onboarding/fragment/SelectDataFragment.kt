package com.hmh.hamyeonham.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hmh.hamyeonham.common.primitive.extractDigits
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.adapter.OnBoardingFragmentType
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentSelectDataBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingSelectDataViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SelectDataFragment : Fragment() {
    private val binding by viewBinding(FragmentSelectDataBinding::bind)
    private val viewModel by viewModels<OnBoardingSelectDataViewModel>()
    private val activityViewModel by activityViewModels<OnBoardingViewModel>()

    private val selectedButtons = mutableSetOf<AppCompatButton>()
    private val fragmentType: OnBoardingFragmentType?
        get() = arguments?.getString(ARG_FRAGMENT_TYPE)?.toOnboardingFragmentType()

    companion object {
        private const val ARG_FRAGMENT_TYPE = "ARG_FRAGMENT_TYPE"

        fun newInstance(fragmentType: OnBoardingFragmentType): SelectDataFragment {
            val onBoardingFragment = SelectDataFragment()
            val args =
                Bundle().apply {
                    putString(ARG_FRAGMENT_TYPE, fragmentType.name)
                }
            onBoardingFragment.arguments = args
            return onBoardingFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentSelectDataBinding.inflate(inflater, container, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initFragmentType()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.sendEvent(OnboardEvent.UpdateNextButtonActive(selectedButtons.isNotEmpty()))
        activityViewModel.sendEvent(OnboardEvent.ChangeActivityButtonText(getString(R.string.all_next)))
        activityViewModel.sendEvent(OnboardEvent.VisibleProgressbar(true))
    }

    private fun initViews() {
        initQuestionButton()
        viewModel.onBoardingSelectDataState.onEach {
            binding.apply {
                val onBoardingQuestion = it.onBoardingQuestion
                tvOnboardingSelectDataQuestion.text = onBoardingQuestion.title
                tvOnboardingSelectDataDescription.text = onBoardingQuestion.description
                btnOnboardingSelectData1.text = onBoardingQuestion.options.getOrNull(0).orEmpty()
                btnOnboardingSelectData2.text = onBoardingQuestion.options.getOrNull(1).orEmpty()
                btnOnboardingSelectData3.text = onBoardingQuestion.options.getOrNull(2).orEmpty()
                btnOnboardingSelectData4.text = onBoardingQuestion.options.getOrNull(3).orEmpty()
            }
        }.launchIn(lifecycleScope)
    }

    private fun initQuestionButton() {
        val onboardingFragmentButtonList = listOf(
            binding.btnOnboardingSelectData1,
            binding.btnOnboardingSelectData2,
            binding.btnOnboardingSelectData3,
            binding.btnOnboardingSelectData4,
        )

        onboardingFragmentButtonList.forEachIndexed { index, button ->
            button.tag = index + 1
            button.setOnClickListener {
                toggleButtonSelection(button)
            }
        }
    }

    private fun initFragmentType() {
        fragmentType?.let {
            viewModel.initQuestionData(it)
        }
    }

    private fun toggleButtonSelection(button: AppCompatButton) {
        button.isSelected = !button.isSelected
        updateSelectedButtons(button)
        updateUserResponse()
    }

    private fun updateSelectedButtons(selectedButton: AppCompatButton) {
        if (selectedButton.isSelected) {
            if (fragmentType == OnBoardingFragmentType.SELECT_DATA_PROBLEM && selectedButtons.size >= 2) {
                val firstSelected = selectedButtons.elementAt(0)
                firstSelected.isSelected = false
                selectedButtons.remove(firstSelected)
            }
            selectedButtons.add(selectedButton)
        } else {
            selectedButtons.remove(selectedButton)
        }

        if (fragmentType != OnBoardingFragmentType.SELECT_DATA_PROBLEM) {
            selectedButtons.filter { it != selectedButton }.forEach { it.isSelected = false }
            selectedButtons.clear()
            if (selectedButton.isSelected) selectedButtons.add(selectedButton)
        }
        activityViewModel.sendEvent(OnboardEvent.UpdateNextButtonActive(selectedButtons.isNotEmpty()))
    }

    private fun updateUserResponse() {
        val selectedQuestion = selectedButtons.map { it.text.toString() }
        val buttonIndices =
            selectedButtons.map { button ->
                button.tag as? Int ?: -1
            }

        val firstSelected = selectedQuestion.firstOrNull()
        val buttonIndex = buttonIndices.firstOrNull() ?: -1

        when (fragmentType) {
            OnBoardingFragmentType.SELECT_DATA_TIME -> {
                activityViewModel.sendEvent(
                    OnboardEvent.UpdateUsuallyUseTime(firstSelected.orEmpty(), buttonIndex),
                )
            }

            OnBoardingFragmentType.SELECT_DATA_PROBLEM -> {
                activityViewModel.sendEvent(
                    OnboardEvent.UpdateProblems(selectedQuestion, buttonIndices),
                )
            }

            OnBoardingFragmentType.SELECT_DATA_PERIOD -> {
                activityViewModel.sendEvent(
                    OnboardEvent.UpdatePeriod(
                        firstSelected?.extractDigits() ?: 0,
                    ),
                )
            }

            else -> {}
        }
    }

    private fun String.toOnboardingFragmentType(): OnBoardingFragmentType =
        try {
            OnBoardingFragmentType.valueOf(this)
        } catch (e: Exception) {
            OnBoardingFragmentType.SELECT_DATA_TIME
        }
}
