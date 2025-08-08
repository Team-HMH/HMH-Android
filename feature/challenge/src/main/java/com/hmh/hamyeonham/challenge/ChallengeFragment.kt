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
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.hmh.hamyeonham.challenge.appadd.AppAddActivity
import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.dialog.TwoButtonCommonDialog
import com.hmh.hamyeonham.common.fragment.viewLifeCycle
import com.hmh.hamyeonham.common.fragment.viewLifeCycleScope
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.view.setOnSingleClickListener
import com.hmh.hamyeonham.common.view.viewBinding
import com.hmh.hamyeonham.core.designsystem.R
import com.hmh.hamyeonham.feature.challenge.databinding.FragmentChallengeBinding
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ChallengeFragment : Fragment() {
    private val binding by viewBinding(FragmentChallengeBinding::bind)
    private val viewModel by viewModels<ChallengeViewModel>()
    private val appSelectionResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                addSelectedApps(result)
            }
        }

    @Inject
    lateinit var navigationProvider: NavigationProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentChallengeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        collectChallengeStateAndProcess()
    }


    private fun collectChallengeStateAndProcess() {
        viewModel.challengeState.flowWithLifecycle(viewLifeCycle).onEach {
            handleModifierButtonState(it.modifierState)
        }.launchIn(viewLifeCycleScope)
    }

    private fun handleModifierButtonState(it: ModifierState) {
        val (text, color) = getTextAndColorsOfModifierState(it)
        binding.tvModifierButton.run {
            this.text = text
            setTextColor(color)
        }
    }

    private fun getTextAndColorsOfModifierState(modifierState: ModifierState) =
        when (modifierState) {
            ModifierState.EDIT -> {
                getString(R.string.all_done) to ContextCompat.getColor(
                    requireContext(),
                    R.color.white_text,
                )
            }

            ModifierState.DONE -> {
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
                    AmplitudeUtils.trackEventWithProperties("click_edit_button")
                    viewModel.updateModifierState(ModifierState.EDIT)
                }

                ModifierState.EDIT -> {
                    viewModel.updateModifierState(ModifierState.DONE)
                }
            }
        }
    }

    private fun initAppAddButton() {
        binding.btGoalAdd.setOnSingleClickListener {
            AmplitudeUtils.trackEventWithProperties("click_add_button")
            val intent = Intent(requireContext(), AppAddActivity::class.java)
            appSelectionResultLauncher.launch(intent)
        }
    }

    private fun initViews() {
        initModifierButton()
        initAppAddButton()
    }


    private fun addSelectedApps(result: ActivityResult) {
        val selectedApps =
            result.data?.getStringArrayExtra(AppAddActivity.SELECTED_APPS)?.toList() ?: return
        val goalTime = result.data?.getLongExtra(AppAddActivity.GOAL_TIME, 0) ?: return
        val apps = Apps.createFromAppCodeListAndGoalTime(selectedApps, goalTime)
        viewModel.addApp(apps)
    }

    private fun RecyclerView.setDeleteAppDialog(it: UsageStatusAndGoal.App) {
        val clickedAppNameToDialog = context.getAppNameFromPackageName(it.packageName)
        TwoButtonCommonDialog.newInstance(
            title = getString(R.string.delete_app_dialog_title, clickedAppNameToDialog),
            description = getString(R.string.delete_app_dialog_description),
            confirmButtonText = getString(R.string.all_delete),
            dismissButtonText = getString(R.string.all_cancel),
        ).apply {
            setConfirmButtonClickListener {
                viewModel.deleteApp(it)
            }
            setDismissButtonClickListener {}
        }.showAllowingStateLoss(childFragmentManager)
    }
}
