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
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentSelectScreentimeBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent

class SelectScreenTimeFragment : Fragment() {
    private val binding by viewBinding(FragmentSelectScreentimeBinding::bind)
    private val activityViewModel by activityViewModels<OnBoardingViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentSelectScreentimeBinding.inflate(
            inflater,
            container,
            false,
        ).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNumberPicker()
    }

    private fun setNumberPicker() {
        binding.run {
            npOnboardingScreentimeGoal.setupScreentimeGoalRange(MINTOTAL, MAXTOTAL)
            npOnboardingScreentimeGoal.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            npOnboardingScreentimeGoal.setOnValueChangedListener { _, _, newTime ->
                activityViewModel.sendEvent(OnboardEvent.UpdateScreenGoalTime(newTime))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.run {
            val screenGoalTime = activityViewModel.onBoardingState.value.screenGoalTime

            binding.npOnboardingScreentimeGoal.value = screenGoalTime
            sendEvent(OnboardEvent.UpdateNextButtonActive(true))
            sendEvent(OnboardEvent.ChangeActivityButtonText(getString(R.string.all_done)))
            sendEvent(OnboardEvent.VisibleProgressbar(true))
        }
    }

    companion object {
        val MINTOTAL = 1
        val MAXTOTAL = 6
    }
}
