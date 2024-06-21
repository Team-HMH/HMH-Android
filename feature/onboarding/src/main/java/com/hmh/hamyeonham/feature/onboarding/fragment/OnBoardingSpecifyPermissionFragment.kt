package com.hmh.hamyeonham.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentOnboardingSpecifyPermissionBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent

class OnBoardingSpecifyPermissionFragment : Fragment() {
    private val activityViewModel by activityViewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentOnboardingSpecifyPermissionBinding.inflate(inflater, container, false).root

    override fun onResume() {
        super.onResume()
        activityViewModel.sendEvent(OnboardEvent.changeActivityButtonText(getString(R.string.all_request_permission)))
    }
}
