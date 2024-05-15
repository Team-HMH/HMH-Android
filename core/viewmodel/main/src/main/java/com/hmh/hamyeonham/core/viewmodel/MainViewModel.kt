package com.hmh.hamyeonham.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.getCurrentDateOfDefaulTimeZone
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.common.time.minusDaysFromDate
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
import kotlinx.datetime.LocalDate
import javax.inject.Inject

data class MainState(
    val appGoals: List<ChallengeStatus.AppGoal> = emptyList(),
    val challengeStatusList: List<ChallengeStatus.Status> = emptyList(),
    val totalGoalTimeInHour: Int = 0,
    val period: Int = 0,
    val todayIndex: Int = 0,
    val usageGoals: List<UsageGoal> = emptyList(),
    val usageStatusAndGoals: List<UsageStatusAndGoal> = emptyList(),
    val name: String = "",
    val point: Int = 0,
    val challengeSuccess: Boolean = true,
) {
    val startDate: LocalDate = minusDaysFromDate(getCurrentDateOfDefaulTimeZone(), todayIndex)
    val isChallengeExist: Boolean = todayIndex != -1

    //~일째를 의미하는 변수
    val todayIndexAsDate: Int = todayIndex + 1
}

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
            getChallengeSuccess()
            getUserInfo()
            getUsageGoalAndStatList()
        }
    }

    fun reloadUsageStatsList() {
        viewModelScope.launch {
            getTodayTimeAndSetUsageStatsList()
        }
    }

    fun updateState(transform: suspend MainState.() -> MainState) {
        viewModelScope.launch {
            val currentState = mainState.value
            val newState = currentState.transform()
            _mainState.value = newState
        }
    }

    fun updateDailyChallengeFailed() {
        viewModelScope.launch {
            challengeRepository.updateDailyChallengeFailed().onSuccess {
                getChallengeStatus()
            }.onFailure {
                Log.e("updateDailyChallengeFailed error", it.toString())
            }
        }
    }

    fun isPointLeftToCollect(): Boolean =
        mainState.value.challengeStatusList.contains(ChallengeStatus.Status.UNEARNED)

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

    private suspend fun getChallengeSuccess() {
        challengeRepository.getTodayResult().onSuccess {
            updateState { copy(challengeSuccess = it) }
        }.onFailure {
            Log.e("getTodayResult error", it.toString())
        }
    }

    private fun getUsageGoalAndStatList() {
        viewModelScope.launch {
            usageGoalsRepository.getUsageGoals().collect {
                setUsageGaols(it)
                getTodayTimeAndSetUsageStatsList()
            }
        }

    }

    private suspend fun getTodayTimeAndSetUsageStatsList() {
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

    fun getUsageStatusAndGoalsExceptTotal(): List<UsageStatusAndGoal> {
        return mainState.value.usageStatusAndGoals.filter { it.packageName != UsageGoal.TOTAL }
    }

    private fun setChallengeStatus(challengeStatus: ChallengeStatus) {
        updateState {
            copy(
                appGoals = challengeStatus.appGoals,
                challengeStatusList = challengeStatus.challengeStatusList,
                totalGoalTimeInHour = challengeStatus.goalTimeInHours,
                period = challengeStatus.period,
                todayIndex = challengeStatus.todayIndex,
            )
        }
    }

    private fun updateUserInfo(userInfo: UserInfo) {
        updateState {
            copy(
                name = userInfo.name,
                point = userInfo.point,
            )
        }
    }

    private fun setUsageStatsList(usageStatsList: List<UsageStatusAndGoal>) {
        updateState {
            copy(usageStatusAndGoals = usageStatsList)
        }
    }
}
