package com.hmh.hamyeonham.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.challenge.usecase.AddUsageGoalsUseCase
import com.hmh.hamyeonham.challenge.usecase.DeleteUsageGoalUseCase
import com.hmh.hamyeonham.challenge.usecase.NewChallengeUseCase
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengeState(
    val modifierState: ModifierState = ModifierState.DONE,
    val usageStatusAndGoals: List<UsageStatusAndGoal> = emptyList(),
) {
    val usageGoalsAndModifiers = usageStatusAndGoals.map {
        ChallengeUsageGoal(it, modifierState)
    }
}

data class ChallengeUsageGoal(
    val usageStatusAndGoal: UsageStatusAndGoal = UsageStatusAndGoal(),
    val modifierState: ModifierState = ModifierState.EDIT,
) {
    companion object {
        const val MAX_DELETABLE = 5
    }

    val isDeletable: Boolean = usageStatusAndGoal.totalTimeInForegroundInMin < MAX_DELETABLE
}


enum class ModifierState {
    EDIT, DONE,
}

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val addUsageGoalsUseCase: AddUsageGoalsUseCase,
    private val deleteUsageGoalUseCase: DeleteUsageGoalUseCase,
    private val newChallengeUseCase: NewChallengeUseCase
) : ViewModel() {

    private val _challengeState = MutableStateFlow(ChallengeState())
    val challengeState = _challengeState.asStateFlow()

    fun updateUsageStatusAndGoals(newUsageStatusAndGoals: List<UsageStatusAndGoal>) {
        updateChallengeState { copy(usageStatusAndGoals = newUsageStatusAndGoals) }
    }

    fun updateModifierState(newModifierState: ModifierState) {
        updateChallengeState { copy(modifierState = newModifierState) }
    }

    fun updateChallengeState(transform: ChallengeState.() -> ChallengeState) {
        val currentState = challengeState.value
        val newState = currentState.transform()
        _challengeState.value = newState
    }

    fun addApp(apps: Apps) {
        viewModelScope.launch {
            addUsageGoalsUseCase(apps)
        }
    }

    fun deleteApp(packageName: String) {
        viewModelScope.launch {
            deleteUsageGoalUseCase(packageName)
        }
    }
    fun generateNewChallenge(newChallenge: NewChallenge) {
        viewModelScope.launch {
            newChallengeUseCase(newChallenge)
        }
    }
}
