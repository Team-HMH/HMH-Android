package com.hmh.hamyeonham.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.domain.main.MainRepository
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usageGoalsRepository: UsageGoalsRepository,
    private val userInfoRepository: UserInfoRepository,
    private val mainRepository: MainRepository,
    private val getUsageStatsListUseCase: GetUsageStatsListUseCase,
    private val updateIsUnLockUseCase: UpdateIsUnLockUseCase,
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


    private val _effect = MutableSharedFlow<MainEffect>()
    val effect = _effect.asSharedFlow()

    init {
        getBanner()
        viewModelScope.launch(Dispatchers.Main) {
            updateIsUnLockUseCase()
        }

        viewModelScope.launch(Dispatchers.Main) {
            getUserInfo()
            getUsageGoalAndStatList()
        }
    }

    fun reloadUsageStatsList() {
        viewModelScope.launch(Dispatchers.Main) {
            getTodayTimeAndSetUsageStatsList()
        }
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

    private fun updateUserInfo(userInfo: UserInfo) {
        updateState {
            copy(name = userInfo.name)
        }
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
}
