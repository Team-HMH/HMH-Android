package com.hmh.hamyeonham.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.usecase.AddUsageGoalsUseCase
import com.hmh.hamyeonham.challenge.usecase.DeleteUsageGoalUseCase
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import com.hmh.hamyeonham.usagestats.usecase.CheckAndDeleteDeletedAppUsageUseCase
import com.hmh.hamyeonham.usagestats.usecase.DeletedAppUsageStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengeState(
    val modifierState: ModifierState = ModifierState.DONE,
)

enum class ModifierState {
    EDIT,
    DONE,
}

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val addUsageGoalsUseCase: AddUsageGoalsUseCase,
    private val deleteUsageGoalUseCase: DeleteUsageGoalUseCase,
    private val deletedAppUsageStoreUseCase: DeletedAppUsageStoreUseCase,
    private val checkAndDeleteDeletedAppUsageUseCase: CheckAndDeleteDeletedAppUsageUseCase,
) : ViewModel() {
    private val _challengeState = MutableStateFlow(ChallengeState())
    val challengeState = _challengeState.asStateFlow()

    fun updateModifierState(newModifierState: ModifierState) {
        updateChallengeState { copy(modifierState = newModifierState) }
    }

    private fun updateChallengeState(transform: ChallengeState.() -> ChallengeState) {
        val currentState = challengeState.value
        val newState = currentState.transform()
        _challengeState.value = newState
    }

    fun addApp(apps: Apps) {
        viewModelScope.launch {
            runCatching {
                addUsageGoalsUseCase(apps)
            }.onSuccess {
                AmplitudeUtils.trackEventWithProperties("complete_add_new")
            }
            checkAndDeleteDeletedAppUsageUseCase(apps.apps.map { it.appCode })
        }
    }

    fun deleteApp(usageStatusAndGoal: UsageStatusAndGoal.App) {
        viewModelScope.launch {
            runCatching {
                deleteUsageGoalUseCase(usageStatusAndGoal.packageName)
            }.onSuccess {
                AmplitudeUtils.trackEventWithProperties("click_delete_complete")
            }
            deletedAppUsageStoreUseCase(
                usageStatusAndGoal.usageTime,
                usageStatusAndGoal.packageName,
            )
        }
    }
}
