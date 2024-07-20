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
import com.hmh.hamyeonham.common.fragment.allPermissionIsGranted
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.service.lockAccessibilityServiceClassName
import com.hmh.hamyeonham.core.viewmodel.MainViewModel
import com.hmh.hamyeonham.feature.main.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val activityViewModel by activityViewModels<MainViewModel>()

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
        initUsageStatsList()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.reloadUsageStatsList()

        activityViewModel.updateIsPermissionGranted(
            allPermissionIsGranted(
                lockAccessibilityServiceClassName,
            ),
        )
    }

    private fun initStaticsRecyclerView() {
        binding.rvStatics.run {
            adapter = UsageStaticsAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }
    }

    private fun initUsageStatsList() {
        val usageStaticsAdapter = binding.rvStatics.adapter as? UsageStaticsAdapter
        activityViewModel.usageStatusAndGoals
            .flowWithLifecycle(viewLifeCycle)
            .onEach { usageStatusGoals ->
                val mainState = activityViewModel.mainState.value
                usageStaticsAdapter?.submitList(
                    usageStatusGoals.map {
                        UsageStaticsModel(
                            userName = mainState.name,
                            challengeSuccess = mainState.challengeSuccess,
                            permissionGranted = mainState.permissionGranted,
                            usageStatusAndGoal = it
                        )
                    }
                )
            }.launchIn(viewLifeCycleScope)
    }
}
