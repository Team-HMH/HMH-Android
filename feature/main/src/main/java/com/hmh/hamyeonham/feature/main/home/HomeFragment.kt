package com.hmh.hamyeonham.feature.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.viewmodel.MainViewModel
import com.hmh.hamyeonham.feature.main.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentHomeBinding.inflate(inflater, container, false).root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initStaticsRecyclerView()
        collectMainState()
        initUsageStatsList()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.reloadUsageStatsList()
    }

    private fun initStaticsRecyclerView() {
        binding.rvStatics.run {
            adapter = UsageStaticsAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }
    }

    private fun collectMainState() {
        activityViewModel.usageStatusAndGoals
            .flowWithLifecycle(viewLifeCycle)
            .onEach { usageStatusGoals ->
                val mainState = activityViewModel.mainState.value
                viewModel.updateHomeState(
                    newUserName = mainState.name,
                    newChallengeSuccess = mainState.challengeSuccess,
                    newUsageStatusAndGoal = usageStatusGoals
                )
            }.launchIn(viewLifeCycleScope)
    }

    private fun initUsageStatsList() {
        val usageStaticsAdapter = binding.rvStatics.adapter as? UsageStaticsAdapter
        viewModel.homeState
            .flowWithLifecycle(viewLifeCycle)
            .onEach { homeState ->
                usageStaticsAdapter?.submitList(
                    homeState.usageStatusAndGoals.map {
                        UsageStaticsModel(
                            userName = homeState.userName,
                            challengeSuccess = homeState.challengeSuccess,
                            usageStatusAndGoal = it,
                            previousUsedPercentage = homeState.previousUsedPercentages.get(it.packageName)
                                ?: 0
                        )
                    }
                )
            }.launchIn(viewLifeCycleScope)
    }
}
