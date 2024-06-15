package com.hmh.hamyeonham.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.domain.point.repository.PointRepository
import com.hmh.hamyeonham.lock.SetIsUnLockUseCase
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatsListUseCase
import com.hmh.hamyeonham.userinfo.model.UserInfo
import com.hmh.hamyeonham.userinfo.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

enum class CalendarToggleState {
    EXPANDED, COLLAPSED,
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val usageGoalsRepository: UsageGoalsRepository,
    private val userInfoRepository: UserInfoRepository,
    private val pointRepository: PointRepository,
    private val getUsageStatsListUseCase: GetUsageStatsListUseCase,
    private val setIsUnLockUseCase: SetIsUnLockUseCase
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    private var rawChallengeStatusList: List<ChallengeStatus.Status> = emptyList()
    private val _challengeStatusList = MutableStateFlow<List<ChallengeStatus.Status>>(emptyList())
    val challengeStatusList = _challengeStatusList.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            uploadSavedChallenge()
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

    fun updateDailyChallengeFailed() {
        viewModelScope.launch {
            pointRepository.usePoint().onSuccess {
                getChallengeStatus()
                setIsUnLockUseCase(true)
                sendEffect(MainEffect.SuccessUsePoint)
            }.onFailure {
                sendEffect(MainEffect.NetworkError)
            }
        }
    }

    fun isPointLeftToCollect(): Boolean =
        challengeStatusList.value.contains(ChallengeStatus.Status.UNEARNED)

    private fun updateState(transform: suspend MainState.() -> MainState) {
        viewModelScope.launch {
            val currentState = mainState.value
            val newState = currentState.transform()
            _mainState.value = newState
        }
    }

    private fun sendEffect(effect: MainEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun uploadSavedChallenge() {
        viewModelScope.launch {
            val challengeWithUsages =
                challengeRepository.getChallengeWithUsage().getOrNull() ?: return@launch
            challengeRepository.uploadSavedChallenge(challengeWithUsages).onSuccess {
                challengeRepository.deleteAllChallengeWithUsage()
            }.onFailure {
                sendEffect(MainEffect.NetworkError)
                Log.e("uploadSavedChallenge error", it.toString())
            }
        }
    }

    private suspend fun updateGoals() {
        usageGoalsRepository.updateUsageGoal()
    }

    private suspend fun getChallengeStatus() {
        challengeRepository.getChallengeData()
            .onSuccess {
                setChallengeStatus(it)
            }.onFailure {
                if (it is HttpException) {
                    when (it.code()) {
                        LACK_POINT_ERROR_CODE -> {
                            sendEffect(MainEffect.LackOfPoint)
                        }

                        else -> sendEffect(MainEffect.NetworkError)
                    }
                } else {
                    sendEffect(MainEffect.NetworkError)
                }
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
            sendEffect(MainEffect.NetworkError)
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
                totalGoalTimeInHour = challengeStatus.goalTimeInHours,
                period = challengeStatus.period,
                todayIndex = challengeStatus.todayIndex,
            )
        }
        rawChallengeStatusList = challengeStatus.challengeStatusList
        _challengeStatusList.value = challengeStatus.challengeStatusList
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

    fun updateChallengeListWithToggleState(calendarToggleState: CalendarToggleState) {
        val challengeStatusList = challengeStatusList.value
        _challengeStatusList.value = when (calendarToggleState) {
            CalendarToggleState.EXPANDED -> {
                rawChallengeStatusList
            }

            CalendarToggleState.COLLAPSED -> {
                challengeStatusList.take(7)
            }
        }
    }

    companion object {
        private const val LACK_POINT_ERROR_CODE = 400
    }
}
