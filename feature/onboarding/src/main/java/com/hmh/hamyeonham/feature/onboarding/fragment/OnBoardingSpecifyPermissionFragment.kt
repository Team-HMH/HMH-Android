package com.hmh.hamyeonham.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentOnboardingSpecifyPermissionBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent

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

//        val text = binding.tvOnboardingSpecifyPermissionList
//        val spanText = SpannableStringBuilder(text.text)
//        spanText.setSpan(
//            TextAppearanceSpan(text.context, com.hmh.hamyeonham.core.designsystem.R.style.TextAppearance_Hmh_LabelLarge),
//            0,
//            39,
//            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        text.text = spanText

        binding.tvPermissionLater.setOnClickListener {
            activityViewModel.updateState { copy(navigateToPermissionView = true) }
        }
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.sendEvent(OnboardEvent.changeActivityButtonText(getString(R.string.all_request_permission)))
    }
}
