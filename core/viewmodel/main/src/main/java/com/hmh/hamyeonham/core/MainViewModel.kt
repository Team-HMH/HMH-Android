package com.hmh.hamyeonham.core

import androidx.lifecycle.ViewModel
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.usagestats.model.UsageGoal
import com.hmh.hamyeonham.usagestats.model.UsageStatAndGoal
import com.hmh.hamyeonham.usagestats.usecase.GetUsageGoalsUseCase
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatsListUseCase
import com.hmh.hamyeonham.userinfo.model.UserInfo
import com.hmh.hamyeonham.userinfo.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainState(
    val challengeStatus: ChallengeStatus = ChallengeStatus(),
    val usageGoals: List<UsageGoal> = emptyList(),
    val usgeStatsList: List<UsageStatAndGoal> = emptyList(),
    val userInfo: UserInfo = UserInfo(),
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUsageGoalsUseCase: GetUsageGoalsUseCase,
    private val usageStatsListUsecase: GetUsageStatsListUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {
    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    init {
        setGoalTimeList(getUsageGoalsUseCase())
        setUsageStatsList()
        setUserInfo(getUserInfoUseCase())
    }

    fun setChallengeStatus(challengeStatus: ChallengeStatus) {
        updateState {
            copy(challengeStatus = challengeStatus)
        }
    }

    fun setGoalTimeList(usageGoal: List<UsageGoal>) {
        updateState {
            copy(usageGoals = usageGoal)
        }
    }

    fun setUserInfo(userInfo: UserInfo) {
        updateState {
            copy(userInfo = userInfo)
        }
    }

    fun updateState(transform: MainState.() -> MainState) {
        val currentState = mainState.value
        val newState = currentState.transform()
        _mainState.value = newState
    }

    private fun setUsageStatsList() {
        val (startTime, endTime) = getCurrentDayStartEndEpochMillis()
        updateState {
            copy(usgeStatsList = usageStatsListUsecase(startTime, endTime))
        }
    }
}
