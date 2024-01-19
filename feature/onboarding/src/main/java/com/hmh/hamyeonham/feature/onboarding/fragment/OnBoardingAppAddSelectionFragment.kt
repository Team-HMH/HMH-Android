package com.hmh.hamyeonham.feature.onboarding.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.feature.onboarding.R
import com.hmh.hamyeonham.feature.onboarding.adapter.OnBoardingAppSelectionAdapter
import com.hmh.hamyeonham.feature.onboarding.databinding.FragmentOnBoardingAppAddSelectionBinding
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingAppSelectionViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnBoardingViewModel
import com.hmh.hamyeonham.feature.onboarding.viewmodel.OnboardEvent
import dagger.hilt.android.AndroidEntryPoint

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
        initSearchBar()
    }

    private fun initViews() {
        initAppSelectionRecyclerAdapter()
    }

    private fun initAppSelectionRecyclerAdapter() {
        binding.rvAppSelection.run {
            adapter = OnBoardingAppSelectionAdapter(
                onAppCheckboxClicked = ::onAppCheckboxClicked,
                onAppCheckboxUnClicked = ::onAppCheckboxUnClicked,
            )
            layoutManager = LinearLayoutManager(requireContext())
        }
        setViewPager()
    }

    private fun setViewPager() {
        val onboardingAppSelectionAdapter =
            binding.rvAppSelection.adapter as? OnBoardingAppSelectionAdapter
        onboardingAppSelectionAdapter?.submitList(viewModel.getInstalledApps())
    }

    private fun onAppCheckboxClicked(packageName: String) {
        activityViewModel.sendEvent(OnboardEvent.AddApps(packageName))
    }

    private fun onAppCheckboxUnClicked(packageName: String) {
        activityViewModel.sendEvent(OnboardEvent.DeleteApp(packageName))
    }

    private fun initSearchBar() {
        binding.etSearchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setRecyclerViewWithFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setRecyclerViewWithFilter(filter: String) {
        val appSelectionAdapter = binding.rvAppSelection.adapter as? OnBoardingAppSelectionAdapter
        val newAppList =
            viewModel.getInstalledApps().filter {
                (context?.getAppNameFromPackageName(it) ?: "").contains(filter)
            }
        for (i in newAppList)
            appSelectionAdapter?.submitList(newAppList)
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.sendEvent(OnboardEvent.changeActivityButtonText(getString(R.string.all_select_done)))
        activityViewModel.sendEvent(OnboardEvent.visibleProgressbar(false))
    }
}
