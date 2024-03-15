package com.hmh.hamyeonham.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.usecase.AddUsageGoalsUseCase
import com.hmh.hamyeonham.challenge.usecase.DeleteUsageGoalUseCase
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengeState(
    val modifierState: ModifierState = ModifierState.EDIT,
    val usageGoals: List<UsageGoal> = emptyList()
) {
    val usageGoalsAndModifierState = usageGoals.map {
        UsageGoalAndModifierState(it, modifierState)
    }
}

data class UsageGoalAndModifierState(
    val usageGoal: UsageGoal = UsageGoal(),
    val modifierState: ModifierState = ModifierState.DONE,
)
//
//data class UsageGoalsState(
//    val usageGoalAndModifierStateList: List<UsageGoalAndModifierState> = emptyList()
//)


enum class ModifierState {
    EDIT, DONE,
}

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val addUsageGoalsUseCase: AddUsageGoalsUseCase,
    private val deleteUsageGoalUseCase: DeleteUsageGoalUseCase,
) : ViewModel() {

    private val _challengeState = MutableStateFlow(ChallengeState())
    val challengeState = _challengeState.asStateFlow()

//    private val _usageGoalsState = MutableStateFlow(UsageGoalsState())
//    val usageGoalsState = _usageGoalsState.asStateFlow()

    fun updateChallengeState(transform: ChallengeState.() -> ChallengeState) {
        val currentState = challengeState.value
        val newState = currentState.transform()
        _challengeState.value = newState
    }

//    fun updateUsageGoalsState(transform: UsageGoalsState.() -> UsageGoalsState) {
//        val currentState = usageGoalsState.value
//        val newState = currentState.transform()
//        _usageGoalsState.value = newState
//    }

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

//    fun setUsageGoalsState(usageGoals: List<UsageGoal>) {
//        updateUsageGoalsState {
//            copy(usageGoalAndModifierStateList = usageGoals.map {
//                UsageGoalAndModifierState(usageGoal = it)
//            })
//        }
//    }
//
//    fun updateUsageGoalsStateWithModifierState(newModifierState: ModifierState) {
//        updateUsageGoalsState {
//            val currentList = usageGoalAndModifierStateList.toMutableList()
//            val newList = currentList.map {
//                it.copy(modifierState = newModifierState)
//            }
//            copy(usageGoalAndModifierStateList = newList)
//        }
//    }

}
