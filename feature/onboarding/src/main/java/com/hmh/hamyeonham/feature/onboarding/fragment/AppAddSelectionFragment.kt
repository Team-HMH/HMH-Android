package com.hmh.hamyeonham.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.adapter.AppSelectionAdapter
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentAppAddSelectionBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingAppSelectionEffect
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingAppSelectionViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AppAddSelectionFragment : Fragment() {
    private val binding by viewBinding(FragmentAppAddSelectionBinding::bind)
    private val viewModel by viewModels<OnBoardingAppSelectionViewModel>()
    private val activityViewModel by activityViewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentAppAddSelectionBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initSearchBar()
        collectState()
        collectEffect()
    }

    private fun initViews() {
        initAppSelectionRecyclerAdapter()
    }

    private fun initAppSelectionRecyclerAdapter() {
        binding.rvAppSelection.run {
            adapter = AppSelectionAdapter(
                onAppCheckboxClicked = ::onAppCheckboxClicked,
                onAppCheckboxUnClicked = ::onAppCheckboxUnClicked,
            )
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun collectState() {
        viewModel.installedApps.flowWithLifecycle(viewLifeCycle).onEach {
            val onboardingAppSelectionAdapter =
                binding.rvAppSelection.adapter as? AppSelectionAdapter
            onboardingAppSelectionAdapter?.submitList(it)
        }.launchIn(viewLifeCycleScope)

        activityViewModel.isAppAddSelectionScreenButtonEnabled
            .flowWithLifecycle(viewLifeCycle, Lifecycle.State.RESUMED)
            .onEach {
                activityViewModel.sendEvent(OnboardEvent.UpdateNextButtonActive(it))
            }.launchIn(viewLifeCycleScope)
    }

    private fun collectEffect() {
        viewModel.effect.flowWithLifecycle(viewLifeCycle).onEach {
            when (it) {
                is OnBoardingAppSelectionEffect.ShowLoading -> {
                    binding.pbLoading.isVisible = true
                }
                is OnBoardingAppSelectionEffect.HideLoading -> {
                    binding.pbLoading.isVisible = false
                }
                is OnBoardingAppSelectionEffect.NONE -> {
                }
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun onAppCheckboxClicked(packageName: String) {
        activityViewModel.sendEvent(OnboardEvent.AddApps(packageName))
    }

    private fun onAppCheckboxUnClicked(packageName: String) {
        activityViewModel.sendEvent(OnboardEvent.DeleteApp(packageName))
    }

    private fun initSearchBar() {
        binding.etSearchbar.doOnTextChanged { text, _, _, _ ->
            viewModel.onQueryChanged(text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.sendEvent(OnboardEvent.ChangeActivityButtonText(getString(R.string.all_select_done)))
        activityViewModel.sendEvent(OnboardEvent.VisibleProgressbar(false))
    }
}
