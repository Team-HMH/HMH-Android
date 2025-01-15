package com.hmh.hamyeonham.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentSelectAppBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent

class SelectAppFragment : Fragment() {
    private val activityViewModel by activityViewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentSelectAppBinding.inflate(inflater, container, false).root

    override fun onResume() {
        super.onResume()
        AmplitudeUtils.trackEventWithProperties("view_challenge_app_time")
        activityViewModel.sendEvent(OnboardEvent.ChangeActivityButtonText(getString(R.string.all_select_app)))
        activityViewModel.sendEvent(OnboardEvent.UpdateNextButtonActive(true))
        activityViewModel.sendEvent(OnboardEvent.VisibleProgressbar(true))
    }
}
