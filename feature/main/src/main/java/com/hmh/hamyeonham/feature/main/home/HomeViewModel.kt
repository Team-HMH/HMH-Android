package com.hmh.hamyeonham.feature.main.home

import androidx.lifecycle.ViewModel
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HomeState(
    val userName: String = "",
    val challengeSuccess: Boolean = true,
    val usageStatusAndGoals: List<UsageStatusAndGoal> = emptyList(),
    val previousUsedPercentages: Map<String, Int> = emptyMap()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    fun updateHomeState(
        newUserName: String,
        newChallengeSuccess: Boolean,
        newUsageStatusAndGoal: List<UsageStatusAndGoal>
    ) {
        updateUserName(newUserName)
        updateChallengeSuccess(newChallengeSuccess)
        updateUsageStatusAndGoal(newUsageStatusAndGoal)
    }

    private fun updateUserName(newUserName: String) {
        updateHomeState { copy(userName = newUserName) }
    }

    private fun updateChallengeSuccess(newChallengeSuccess: Boolean) {
        updateHomeState { copy(challengeSuccess = newChallengeSuccess) }
    }

    private fun updateUsageStatusAndGoal(newUsageStatusAndGoal: List<UsageStatusAndGoal>) {
        val newPreviousUsedPercentage =
            homeState.value.usageStatusAndGoals.associate { it.packageName to it.usedPercentage }
        updatePreviousUsedPercentage(newPreviousUsedPercentage)
        updateHomeState { copy(usageStatusAndGoals = newUsageStatusAndGoal) }
    }

    private fun updatePreviousUsedPercentage(newPreviousUsedPercentage: Map<String, Int>) {
        updateHomeState { copy(previousUsedPercentages = newPreviousUsedPercentage) }
    }

    private fun updateHomeState(transform: HomeState.() -> HomeState) {
        val currentState = homeState.value
        val newState = currentState.transform()

        _homeState.value = newState
    }

}