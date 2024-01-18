package com.hmh.hamyeonham.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.challenge.model.Status
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatsListUseCase
import com.hmh.hamyeonham.userinfo.model.UserInfo
import com.hmh.hamyeonham.userinfo.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainState(
    val appGoals: List<ChallengeStatus.AppGoal> = emptyList(),
    val isSuccessList: List<Status> = emptyList(),
    val goalTime: Int = 0,
    val period: Int = 0,
    val usageGoals: List<UsageGoal> = emptyList(),
    val usageStatsList: List<UsageStatusAndGoal> = emptyList(),
    val name: String = "",
    val point: Int = 0,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val usageGoalsRepository: UsageGoalsRepository,
    private val getUsageStatsListUseCase: GetUsageStatsListUseCase,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    init {
        viewModelScope.launch {
            updateGoals()
            getChallengeStatus()
            getUserInfo()
        }
        getUsageGoalAndStatList()
    }

    private suspend fun updateGoals() {
        usageGoalsRepository.updateUsageGoal()
    }

    private suspend fun getChallengeStatus() {
        challengeRepository.getChallengeData().onSuccess {
            setChallengeStatus(it)
        }.onFailure {
            Log.e("challenge status error", it.toString())
        }
    }

    private fun getUsageGoalAndStatList() {
        viewModelScope.launch {
            usageGoalsRepository.getUsageGoals().collect {
                setUsageGaols(it)
                getStatsList()
            }
        }
    }

    private suspend fun getStatsList() {
        val (startTime, endTime) = getCurrentDayStartEndEpochMillis()
        setUsageStatsList(getUsageStatsListUseCase(startTime, endTime))
    }

    private suspend fun getUserInfo() {
        userInfoRepository.getUserInfo().onSuccess {
            updateUserInfo(it)
        }.onFailure {
            Log.e("userInfo error", it.toString())
        }
    }

    private fun setUsageGaols(usageGoals: List<UsageGoal>) {
        updateState {
            copy(usageGoals = usageGoals)
        }
    }

    fun getUsageGoalsExceptTotal(): List<UsageGoal> {
        return mainState.value.usageGoals.filter { it.packageName != UsageGoal.TOTAL }
    }

    fun setChallengeStatus(challengeStatus: ChallengeStatus) {
        updateState {
            copy(
                appGoals = challengeStatus.appGoals,
                isSuccessList = challengeStatus.isSuccessList,
                goalTime = challengeStatus.goalTime,
                period = challengeStatus.period,
            )
        }
    }

    fun updateUserInfo(userInfo: UserInfo) {
        updateState {
            copy(
                name = userInfo.name,
                point = userInfo.point,
            )
        }
    }

    fun updateState(transform: suspend MainState.() -> MainState) {
        viewModelScope.launch {
            val currentState = mainState.value
            val newState = currentState.transform()
            _mainState.value = newState
        }
    }

    private fun setUsageStatsList(usageStatsList: List<UsageStatusAndGoal>) {
        updateState {
            copy(usageStatsList = usageStatsList)
        }
    }
}
