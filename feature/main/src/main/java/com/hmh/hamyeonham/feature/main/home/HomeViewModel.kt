package com.hmh.hamyeonham.feature.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.core.viewmodel.HomeItem
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeState(
    val userName: String = "",
    val challengeSuccess: Boolean = true,
    val usageStatusAndGoals: UsageStatusAndGoal = UsageStatusAndGoal(),
    val previousUsedPercentages: Map<String, Int> = emptyMap()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    private val homeState = MutableStateFlow(HomeState())

    val homeItems = homeState.map { homeState ->
        listOf(
            HomeItem.TotalModel(
                userName = homeState.userName,
                challengeSuccess = homeState.challengeSuccess,
                totalGoalTime = homeState.usageStatusAndGoals.totalGoalTime,
                totalTimeInForeground = homeState.usageStatusAndGoals.totalTimeInForeground,
                usageAppStatusAndGoal = homeState.usageStatusAndGoals.apps.firstOrNull()
                    ?: UsageStatusAndGoal.App(),
                previousUsedPercentage = 0
            )
        ) + homeState.usageStatusAndGoals.apps.map { apps ->
            HomeItem.UsageStaticsModel(
                usageAppStatusAndGoal = apps,
                previousUsedPercentage = homeState.previousUsedPercentages.getOrDefault(
                    apps.packageName,
                    0
                )
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateHomeState(
        newUserName: String,
        newChallengeSuccess: Boolean,
        newUsageStatusAndGoal: UsageStatusAndGoal
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

    private fun updateUsageStatusAndGoal(newUsageStatusAndGoal: UsageStatusAndGoal) {
        val newPreviousUsedPercentage =
            homeState.value.usageStatusAndGoals.apps.associate { it.packageName to it.usedPercentage }
        updatePreviousUsedPercentage(newPreviousUsedPercentage)
        updateHomeState { copy(usageStatusAndGoals = newUsageStatusAndGoal) }
    }

    private fun updatePreviousUsedPercentage(newPreviousUsedPercentage: Map<String, Int>) {
        updateHomeState { copy(previousUsedPercentages = newPreviousUsedPercentage) }
    }

    private fun updateHomeState(transform: HomeState.() -> HomeState) {
        val currentState = homeState.value
        val newState = currentState.transform()

        homeState.value = newState
    }

}