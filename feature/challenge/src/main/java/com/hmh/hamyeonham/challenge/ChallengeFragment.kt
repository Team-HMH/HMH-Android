package com.hmh.hamyeonham.challenge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmh.hamyeonham.challenge.appadd.AppAddActivity
import com.hmh.hamyeonham.challenge.calendar.ChallengeCalendarAdapter
import com.hmh.hamyeonham.challenge.goals.ChallengeUsageGoalsAdapter
import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.view.VerticalSpaceItemDecoration
import com.hmh.hamyeonham.common.view.dp
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.designsystem.R
import com.hmh.hamyeonham.core.viewmodel.MainViewModel
import com.hmh.hamyeonham.feature.challenge.databinding.FragmentChallengeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ChallengeFragment : Fragment() {
    private val binding by viewBinding(FragmentChallengeBinding::bind)
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<ChallengeViewModel>()
    private lateinit var appSelectionResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentChallengeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initResultLauncher()
        collectMainState()
        collectChallengeState()
    }

    private fun collectChallengeState() {
        viewModel.challengeState.flowWithLifecycle(viewLifeCycle).onEach {
            handleModifierButtonState(it)
        }.launchIn(viewLifeCycleScope)
    }

    private fun handleModifierButtonState(it: ChallengeState) {
        val (text, color) = when (it.modifierState) {
            ModifierState.CANCEL -> {
                getString(R.string.all_cancel) to ContextCompat.getColor(
                    requireContext(),
                    R.color.white_text
                )
            }

            ModifierState.DELETE -> {
                getString(R.string.all_delete) to ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_purple_text
                )
            }
        }
        binding.tvModifierButton.run {
            this.text = text
            setTextColor(color)
        }
    }

    private fun initViews() {
        initChallengeRecyclerView()
        initChallengeGoalsRecyclerView()
        initModifierButton()
    }

    private fun initModifierButton() {
        binding.tvModifierButton.setOnClickListener {
            when (viewModel.challengeState.value.modifierState) {
                ModifierState.CANCEL -> {
                    viewModel.updateState {
                        copy(modifierState = ModifierState.DELETE)
                    }
                }

                ModifierState.DELETE -> {
                    viewModel.updateState {
                        copy(modifierState = ModifierState.CANCEL)
                    }
                }
            }
        }
    }

    private fun collectMainState() {
        val challengeAdapter = binding.rvChallengeCalendar.adapter as? ChallengeCalendarAdapter
        val challengeGoalsAdapter = binding.rvAppUsageGoals.adapter as? ChallengeUsageGoalsAdapter
        activityViewModel.mainState.flowWithLifecycle(viewLifeCycle).onEach {
            challengeAdapter?.submitList(it.challengeStatus.isSuccessList)
            if (it.usageGoals.isNotEmpty()) {
                challengeGoalsAdapter?.submitList(it.usageGoals.drop(1) + UsageGoal())
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initChallengeRecyclerView() {
        binding.rvChallengeCalendar.run {
            layoutManager = GridLayoutManager(requireContext(), 7)
            adapter = ChallengeCalendarAdapter()
        }
    }

    private fun initResultLauncher() {
        appSelectionResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedApps =
                        result.data?.getStringArrayExtra(AppAddActivity.SELECTED_APPS)
                    val goalTime = result.data?.getLongExtra(AppAddActivity.GOAL_TIME, 0)
                    val apps = Apps(
                        apps = selectedApps?.map {
                            Apps.App(
                                appCode = it,
                                goalTime = (goalTime ?: 0).toInt()
                            )
                        } ?: return@registerForActivityResult
                    )
                    viewModel.addApp(apps)
                }
            }
    }

    private fun initChallengeGoalsRecyclerView() {
        binding.rvAppUsageGoals.run {
            adapter = ChallengeUsageGoalsAdapter(
                onAppListAddClicked = {
                    val intent = Intent(requireContext(), AppAddActivity::class.java)
                    appSelectionResultLauncher.launch(intent)
                },
                onAppItemClicked = {
                    when (viewModel.challengeState.value.modifierState) {
                        ModifierState.DELETE -> {
                            viewModel.deleteApp(it.packageName)
                        }

                        else -> Unit
                    }
                },
            )
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(VerticalSpaceItemDecoration(9.dp))
        }
    }
}
