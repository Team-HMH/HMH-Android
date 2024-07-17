package com.hmh.hamyeonham.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentOnboardingSpecifyPermissionBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OnBoardingSpecifyPermissionFragment : Fragment() {
    private val binding by viewBinding(FragmentOnboardingSpecifyPermissionBinding::bind)
    private val activityViewModel by activityViewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentOnboardingSpecifyPermissionBinding.inflate(inflater, container, false).root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPermissionLater.setOnClickListener {
            activityViewModel.sendEvent(OnboardEvent.NavigateToPermissionView(true))
        }
        collectState()
    }

    private fun collectState() {
        activityViewModel.isAccessibilityServicePermissionScreenButtonEnabled
            .flowWithLifecycle(viewLifeCycle, Lifecycle.State.RESUMED)
            .onEach {
                activityViewModel.sendEvent(OnboardEvent.UpdateNextButtonActive(it))
            }.launchIn(viewLifeCycleScope)
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.sendEvent(OnboardEvent.ChangeActivityButtonText(getString(R.string.all_request_permission)))
        activityViewModel.sendEvent(OnboardEvent.UpdateBackButtonActive(false))
    }
}
