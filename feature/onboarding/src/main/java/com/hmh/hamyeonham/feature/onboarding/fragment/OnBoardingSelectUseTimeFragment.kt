package com.hmh.hamyeonham.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.common.view.setupScreentimeGoalRange
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentOnBoardingSelectUseTimeBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent

class OnBoardingSelectUseTimeFragment : Fragment() {
    private val binding by viewBinding(FragmentOnBoardingSelectUseTimeBinding::bind)
    private val activityViewModel by activityViewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentOnBoardingSelectUseTimeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNumberPicker()
        handleNumberPickerValue()
    }

    private fun handleNumberPickerValue() {
        binding.npOnboardingUseTimeGoalHour.setOnValueChangedListener { _, _, hour ->
            activityViewModel.sendEvent(OnboardEvent.UpdateAppGoalTimeHour(hour))
        }
        binding.npOnboardingUseTimeGoalMinute.setOnValueChangedListener { _, _, minute ->
            activityViewModel.sendEvent(OnboardEvent.UpdateAppGoalTimeMinute(minute))
        }
    }

    private fun setNumberPicker() {
        binding.run {
            npOnboardingUseTimeGoalHour.setupScreentimeGoalRange(0, 1)
            npOnboardingUseTimeGoalMinute.setupScreentimeGoalRange(0, 59)
            npOnboardingUseTimeGoalHour.descendantFocusability =
                NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }
    }
    override fun onResume() {
        super.onResume()
        val selectedHour = activityViewModel.onBoardingState.value.appGoalTimeHour
        val selectedMinute = activityViewModel.onBoardingState.value.appGoalTimeMinute

        binding.npOnboardingUseTimeGoalHour.value = selectedHour
        binding.npOnboardingUseTimeGoalMinute.value = selectedMinute
        activityViewModel.sendEvent(OnboardEvent.ChangeActivityButtonText(getString(R.string.all_done)))
        activityViewModel.sendEvent(OnboardEvent.VisibleProgressbar(true))
    }
}
