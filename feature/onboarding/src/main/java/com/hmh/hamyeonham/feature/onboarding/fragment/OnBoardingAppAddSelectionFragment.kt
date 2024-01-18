package com.hmh.hamyeonham.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.adapter.OnBoardingAppSelectionAdapter
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentOnBoardingAppAddSelectionBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingAppSelectionViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OnBoardingAppAddSelectionFragment : Fragment() {
    private val binding by viewBinding(FragmentOnBoardingAppAddSelectionBinding::bind)
    private val viewModel by viewModels<OnBoardingAppSelectionViewModel>()
    private val activityViewModel by activityViewModels<OnBoardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentOnBoardingAppAddSelectionBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        initAppSelectionRecyclerAdapter()
        collectState()
    }

    private fun initAppSelectionRecyclerAdapter() {
        binding.rvAppSelection.run {
            adapter = OnBoardingAppSelectionAdapter(
                onAppCheckboxClicked = ::onAppCheckboxClicked,
                onAppCheckboxUnClicked = ::onAppCheckboxUnClicked,
            )
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun collectState() {
        viewModel.state.flowWithLifecycle(lifecycle).onEach { state ->
            val appSelectionAdapter =
                binding.rvAppSelection.adapter as? OnBoardingAppSelectionAdapter
            appSelectionAdapter?.submitList(
                viewModel.getInstalledApps().map {
                    Pair(
                        it,
                        checkIfAppIsSelected(it),
                    )
                },
            )
        }.launchIn(lifecycleScope)
    }

    private fun checkIfAppIsSelected(app: String): Boolean {
        val selcetedApps = viewModel.state.value.selectedApp
        return selcetedApps.find { it == app } != null
    }

    private fun onAppCheckboxClicked(packageName: String) {
        activityViewModel.sendEvent(OnboardEvent.AddApps(packageName))
        viewModel.updateState {
            copy(selectedApp = selectedApp + packageName)
        }
    }

    private fun onAppCheckboxUnClicked(packageName: String) {
        activityViewModel.sendEvent(OnboardEvent.DeleteApp(packageName))
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.sendEvent(OnboardEvent.changeActivityButtonText(getString(R.string.all_select_done)))
        activityViewModel.sendEvent(OnboardEvent.visibleProgressbar(false))
    }
}
