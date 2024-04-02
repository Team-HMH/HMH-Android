package com.hmh.hamyeonham.challenge

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.challenge.appadd.AppAddActivity
import com.hmh.hamyeonham.challenge.calendar.ChallengeCalendarAdapter
import com.hmh.hamyeonham.challenge.goals.ChallengeUsageGoalsAdapter
import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.dialog.TwoButtonCommonDialog
import com.hmh.hamyeonham.common.fragment.snackBarWithAction
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.VerticalSpaceItemDecoration
import com.hmh.hamyeonham.common.view.dp
import com.hmh.hamyeonham.common.view.mapBooleanToVisibility
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.designsystem.R
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.viewmodel.MainViewModel
import com.hmh.hamyeonham.feature.challenge.databinding.FragmentChallengeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class ChallengeFragment : Fragment() {
    private val binding by viewBinding(FragmentChallengeBinding::bind)
    private val activityViewModel by activityViewModels<MainViewModel>()
    private val viewModel by viewModels<ChallengeViewModel>()
    private lateinit var appSelectionResultLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentChallengeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppSelectionResultLauncher()
        initViews()
        initChallengeCalendar()
        initUsageGoalList()
    }

    private fun initChallengeCalendar() {
        activityViewModel.collectMainState(viewLifeCycle).onEach {
            setChallengeInfoVisibility(it.isChallengeExist)
            if (it.usageGoals.isNotEmpty()) viewModel.updateChallengeState { copy(usageGoals = (activityViewModel.getUsageGoalsExceptTotal() + UsageGoal())) }
            if (it.isChallengeExist) {
                bindChallengeCalendar(it.challengeStatusList)
                bindChallengeDate(it.todayIndex, it.startDate)
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initUsageGoalList() {
        viewModel.collectChallengeState(viewLifeCycle).onEach {
            handleModifierButtonState(it)
            bindUsageGoals(it.usageGoalsAndModifierState)
        }.launchIn(viewLifeCycleScope)
    }


    private fun handleModifierButtonState(it: ChallengeState) {
        val (text, color) = getTextAndColorsOfModifierState(it.modifierState)
        binding.tvModifierButton.run {
            this.text = text
            setTextColor(color)
        }
    }

    private fun getTextAndColorsOfModifierState(modifierState: ModifierState) =
        when (modifierState) {
            ModifierState.DONE -> {
                getString(R.string.all_done) to ContextCompat.getColor(
                    requireContext(),
                    R.color.white_text,
                )
            }

            ModifierState.EDIT -> {
                getString(R.string.all_edit) to ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_purple_text,
                )
            }
        }

    private fun initModifierButton() {
        binding.tvModifierButton.setOnClickListener {
            when (viewModel.challengeState.value.modifierState) {
                ModifierState.DONE -> {
                    viewModel.updateChallengeState {
                        copy(modifierState = ModifierState.EDIT)
                    }
                }

                ModifierState.EDIT -> {
                    viewModel.updateChallengeState {
                        copy(modifierState = ModifierState.DONE)
                    }
                }
            }
        }
    }

    private fun initChallengeCreateButton() {
        binding.btnChallengeCreate.setOnClickListener {
            if (activityViewModel.isPointLeftToCollect()) {
                snackBarWithAction(
                    anchorView = binding.root,
                    message = getString(com.hmh.hamyeonham.feature.challenge.R.string.challenge_cannot_create),
                    actionMessage = getString(
                        com.hmh.hamyeonham.feature.challenge.R.string.all_move
                    )
                ) {
                    //TODO 포인트 받기 뷰로 이동
                }
            }
            else {
                //TODO 챌린지 생성 기능 추가
            }
        }
    }

    private fun initViews() {
        initModifierButton()
        initChallengeCreateButton()
        initChallengeGoalsRecyclerView()
        initChallengeCalendarRecyclerView()
    }

    private fun setChallengeInfoVisibility(isChallengeExist: Boolean) {
        binding.btnChallengeCreate.visibility = (!isChallengeExist).mapBooleanToVisibility()
        binding.tvChallengeCreateTitle.visibility = (!isChallengeExist).mapBooleanToVisibility()
        binding.tvChallengeDay.visibility = isChallengeExist.mapBooleanToVisibility()
        binding.tvChallengeStartDate.visibility = isChallengeExist.mapBooleanToVisibility()
        binding.rvChallengeCalendar.visibility = isChallengeExist.mapBooleanToVisibility()
    }

    private fun bindUsageGoals(usageGoalAndModifierStateList: List<UsageGoalAndModifierState>) {
        val challengeGoalsAdapter = binding.rvAppUsageGoals.adapter as? ChallengeUsageGoalsAdapter
        challengeGoalsAdapter?.submitList(usageGoalAndModifierStateList)
    }

    private fun bindChallengeCalendar(challengeStatusList: List<ChallengeStatus.Status>) {
        val challengeAdapter = binding.rvChallengeCalendar.adapter as? ChallengeCalendarAdapter
        challengeAdapter?.submitList(challengeStatusList)
    }

    private fun bindChallengeDate(todayIndex: Int, startDate: LocalDate) {
        binding.tvChallengeStartDate.text = getString(
            com.hmh.hamyeonham.feature.challenge.R.string.challenge_start_date,
            startDate.monthNumber,
            startDate.dayOfMonth
        )
        binding.tvChallengeDay.text =
            getString(com.hmh.hamyeonham.feature.challenge.R.string.challenge_day, todayIndex)
    }

    private fun initChallengeCalendarRecyclerView() {
        binding.rvChallengeCalendar.run {
            layoutManager = GridLayoutManager(requireContext(), 7)
            adapter = ChallengeCalendarAdapter(context)
        }
    }

    private fun initAppSelectionResultLauncher() {
        appSelectionResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    addSelectedApps(result)
                }
            }
    }

    private fun addSelectedApps(result: ActivityResult) {
        val selectedApps =
            result.data?.getStringArrayExtra(AppAddActivity.SELECTED_APPS)?.toList() ?: return
        val goalTime = result.data?.getLongExtra(AppAddActivity.GOAL_TIME, 0) ?: return
        val apps = Apps.createFromAppCodeListAndGoalTime(selectedApps, goalTime)
        viewModel.addApp(apps)
    }

    private fun initChallengeGoalsRecyclerView() {
        binding.rvAppUsageGoals.run {
            adapter = ChallengeUsageGoalsAdapter(
                onAppListAddClicked = {
                    val intent = Intent(requireContext(), AppAddActivity::class.java)
                    appSelectionResultLauncher.launch(intent)
                },
                onAppItemClicked = { usagUsageGoalAndModifierState ->
                    when (viewModel.challengeState.value.modifierState) {
                        ModifierState.DONE -> {
                            setDeleteAppDialog(usagUsageGoalAndModifierState.usageGoal)
                        }

                        else -> Unit
                    }
                },
            )
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(VerticalSpaceItemDecoration(9.dp))
        }
    }

    private fun RecyclerView.setDeleteAppDialog(it: UsageGoal) {
        val clickedAppNameToDialog = context.getAppNameFromPackageName(it.packageName)
        TwoButtonCommonDialog.newInstance(
            title = getString(R.string.delete_app_dialog_title, clickedAppNameToDialog),
            description = getString(R.string.delete_app_dialog_description),
            confirmButtonText = getString(R.string.all_okay),
            dismissButtonText = getString(R.string.all_cancel),
        ).apply {
            setConfirmButtonClickListener {
                viewModel.deleteApp(it.packageName)
            }
            setDismissButtonClickListener {}
        }.showAllowingStateLoss(childFragmentManager)
        viewModel.updateChallengeState {
            copy(modifierState = ModifierState.EDIT)
        }
    }
}
