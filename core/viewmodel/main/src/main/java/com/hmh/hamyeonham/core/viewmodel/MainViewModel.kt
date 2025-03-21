package com.hmh.hamyeonham.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.Challenge
import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.challenge.usecase.NewChallengeUseCase
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.domain.main.MainRepository
import com.hmh.hamyeonham.domain.point.repository.PointRepository
import com.hmh.hamyeonham.lock.SetIsUnLockUseCase
import com.hmh.hamyeonham.lock.UpdateIsUnLockUseCase
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatsListUseCase
import com.hmh.hamyeonham.userinfo.model.UserInfo
import com.hmh.hamyeonham.userinfo.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
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
    private val mainRepository: MainRepository,
    private val getUsageStatsListUseCase: GetUsageStatsListUseCase,
    private val setIsUnLockUseCase: SetIsUnLockUseCase,
    private val updateIsUnLockUseCase: UpdateIsUnLockUseCase,
    private val newChallengeUseCase: NewChallengeUseCase,
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    private val _usageStatusAndGoals = MutableStateFlow(UsageStatusAndGoal())
    val usageStatusAndGoals = _usageStatusAndGoals.asStateFlow()

    private val banner = MutableStateFlow<HomeItem.BannerModel?>(null)

    val homeItems = combine(
        banner,
        usageStatusAndGoals
    ) { bannerModel, usageStatusAndGoals ->
        combineHomeItems(bannerModel, usageStatusAndGoals)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    private var rawChallengeList: List<ChallengeStatus> = emptyList()
    private val _challengeList = MutableStateFlow<List<ChallengeStatus>>(emptyList())
    val challengeStatusList = _challengeList.asStateFlow()

    private val _userPoint = MutableStateFlow(0)
    val userPoint = _userPoint.asStateFlow()

    val isPointLeftToCollect
        get() =
            challengeStatusList.value.contains(ChallengeStatus.UNEARNED)


    private val _effect = MutableSharedFlow<MainEffect>()
    val effect = _effect.asSharedFlow()

    init {
        getBanner()
        viewModelScope.launch(Dispatchers.Main) {
            updateIsUnLockUseCase()
        }

        viewModelScope.launch(Dispatchers.Main) {
            updateGoals()
            getChallengeStatus()
            getUserInfo()
            getUsageGoalAndStatList()
        }
    }

    fun reloadChallengeStatus() {
        viewModelScope.launch(Dispatchers.Main) {
            getChallengeStatus()
        }
    }

    fun reloadUsageStatsList() {
        viewModelScope.launch(Dispatchers.Main) {
            getTodayTimeAndSetUsageStatsList()
        }
    }

    fun updateDailyChallengeFailed() {
        viewModelScope.launch(Dispatchers.Main) {
            pointRepository.usePoint().onSuccess {
                _userPoint.value = it.userPoint
                setIsUnLockUseCase(true).onSuccess {
                    getChallengeStatus()
                    sendEffect(MainEffect.SuccessUsePoint)
                }.onFailure { e ->
                    Timber.e(e)
                    sendEffect(MainEffect.NetworkError)
                }
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
            }
        }
    }

    fun generateNewChallenge(newChallenge: NewChallenge) {
        viewModelScope.launch(Dispatchers.Main) {
            newChallengeUseCase(newChallenge).onSuccess {
                getChallengeStatus()
            }
        }
    }

    fun updateChallengeListWithToggleState(calendarToggleState: CalendarToggleState) {
        val challengeStatusList = challengeStatusList.value
        _challengeList.value = when (calendarToggleState) {
            CalendarToggleState.EXPANDED -> {
                rawChallengeList
            }

            CalendarToggleState.COLLAPSED -> {
                if (challengeStatusList.size == 14)
                    challengeStatusList.take(14)
                else
                    challengeStatusList.take(7)
            }
        }
    }

    fun updatePoint(point: Int) {
        _userPoint.value = point
    }

    private fun updateState(transform: suspend MainState.() -> MainState) {
        viewModelScope.launch(Dispatchers.Main) {
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

    private suspend fun updateGoals() {
        usageGoalsRepository.updateUsageGoal()
            .onSuccess {
                updateState { copy(challengeSuccess = it) }
            }
    }

    private suspend fun getChallengeStatus() {
        challengeRepository.getChallengeData()
            .onSuccess {
                setChallengeStatus(it)
            }.onFailure {
                if (it is HttpException) {
                    sendEffect(MainEffect.NetworkError)
                } else {
                    sendEffect(MainEffect.NetworkError)
                }
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
            Timber.tag("userInfo error").e(it.toString())
        }
    }

    private fun setUsageGaols(usageGoals: UsageGoal) {
        updateState {
            copy(usageGoals = usageGoals)
        }
    }

    private fun setChallengeStatus(challenge: Challenge) {
        updateState {
            copy(
                appGoals = challenge.appGoals,
                totalGoalTimeInHour = challenge.goalTimeInHours,
                period = challenge.period,
                todayIndex = challenge.todayIndex,
            )
        }
        rawChallengeList = challenge.challengeList
        _challengeList.value = challenge.challengeList
    }

    private fun updateUserInfo(userInfo: UserInfo) {
        updateState {
            copy(name = userInfo.name)
        }
        _userPoint.value = userInfo.point
    }

    private fun setUsageStatsList(usageStatsList: UsageStatusAndGoal) {
        _usageStatusAndGoals.value = usageStatsList
    }

    private fun getBanner() {
        viewModelScope.launch {
            val bannerData = mainRepository
                .getBanner()
                .getOrNull()
            if (bannerData == null) return@launch
            if (bannerData.imageUrl.isBlank()) return@launch
            if (bannerData.title.isBlank()) return@launch

            banner.value = bannerData.toBannerModel()
        }
    }

    private fun combineHomeItems(
        banner: HomeItem.BannerModel?,
        usageStatusAndGoal: UsageStatusAndGoal
    ): List<HomeItem> {
        val items = mutableListOf<HomeItem>()

        items.add(
            HomeItem.TotalModel(
                userName = mainState.value.name,
                challengeSuccess = mainState.value.challengeSuccess,
                totalGoalTime = usageStatusAndGoal.totalGoalTime,
                totalTimeInForeground = usageStatusAndGoal.totalTimeInForeground,
            )
        )

        banner?.let {
            items.add(it)
        }

        items.addAll(
            usageStatusAndGoal.apps.map { apps ->
                HomeItem.UsageStaticsModel(apps)
            }
        )

        return items
    }


    companion object {
        private const val LACK_POINT_ERROR_CODE = 400
    }
}
