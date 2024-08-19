package com.hmh.hamyeonham.feature.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.common.time.timeToMs
import com.hmh.hamyeonham.core.network.auth.datastore.network.HMHNetworkPreference
import com.hmh.hamyeonham.login.model.SignRequestDomain
import com.hmh.hamyeonham.login.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OnboardEvent {
    data class UpdateUsuallyUseTime(
        val usuallyUseTime: String,
        val buttonIndex: Int,
    ) : OnboardEvent

    data class UpdateProblems(
        val problems: List<String>,
        val buttonIndices: List<Int>,
    ) : OnboardEvent

    data class UpdatePeriod(
        val period: Int,
    ) : OnboardEvent

    data class UpdateScreenGoalTime(
        val screeGoalTime: Int,
    ) : OnboardEvent

    data class AddApps(
        val appCode: String,
    ) : OnboardEvent

    data class DeleteApp(
        val appCode: String,
    ) : OnboardEvent

    data class UpdateAppGoalTimeMinute(
        val goalTimeMinute: Int,
    ) : OnboardEvent

    data class UpdateAppGoalTimeHour(
        val goalTimeHour: Int,
    ) : OnboardEvent

    data class UpdateNextButtonActive(
        val isNextButtonActive: Boolean,
    ) : OnboardEvent

    data class UpdateAccessToken(
        val accessToken: String,
    ) : OnboardEvent

    data class ChangeActivityButtonText(
        val buttonText: String,
    ) : OnboardEvent

    data class VisibleProgressbar(
        val progressbarVisible: Boolean,
    ) : OnboardEvent

    data class UpdateBackButtonActive(
        val isBackButtonActive: Boolean,
    ) : OnboardEvent
}

sealed interface OnboardEffect {
    data object OnboardSuccess : OnboardEffect

    data object OnboardFail : OnboardEffect
}

data class OnBoardingState(
    val usuallyUseTime: String = "",
    val usuallyUseTimeButtonIndex: Int = -1,
    val problems: List<String> = emptyList(),
    val problemsButtonIndex: List<Int> = emptyList(),
    val period: Int = -1,
    val screenGoalTime: Int = DEFAULT_SCREEN_TIME,
    val appCodeList: List<String> = emptyList(),
    val appGoalTimeMinute: Int = 0,
    val appGoalTimeHour: Int = 0,
    val isNextButtonActive: Boolean = false,
    val isBackButtonActive: Boolean = true,
    val accessToken: String = "",
    val buttonText: String = "다음",
    val progressbarVisible: Boolean = true,
) {
    val goalTime: Long
        get() = (screenGoalTime * 60).timeToMs()
    val appGoalTime: Long
        get() = ((appGoalTimeHour * 60) + appGoalTimeMinute).timeToMs()

    companion object {
        const val DEFAULT_SCREEN_TIME: Int = 1
    }
}

@HiltViewModel
class OnBoardingViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val hmhNetworkPreference: HMHNetworkPreference,
    ) : ViewModel() {
        private val _onBoardingState = MutableStateFlow(OnBoardingState())
        val onBoardingState = _onBoardingState.asStateFlow()

        private val _onboardEffect = MutableSharedFlow<OnboardEffect>()
        val onboardEffect = _onboardEffect.asSharedFlow()

        val isAppAddSelectionScreenButtonEnabled =
            onBoardingState
                .map { onBoardingState ->
                    onBoardingState.appCodeList.isNotEmpty()
                }.stateIn(viewModelScope, SharingStarted.Lazily, false)

        val isUseTimeScreenButtonEnabled =
            onBoardingState
                .map { onBoardingState ->
                    onBoardingState.appGoalTimeHour > 0 || onBoardingState.appGoalTimeMinute > 0
                }.stateIn(viewModelScope, SharingStarted.Lazily, false)

        private fun updateState(transform: OnBoardingState.() -> OnBoardingState) {
            val currentState = onBoardingState.value
            val newState = currentState.transform()
            _onBoardingState.value = newState
        }

        fun sendEvent(event: OnboardEvent) {
            when (event) {
                is OnboardEvent.UpdateUsuallyUseTime -> {
                    updateState {
                        copy(usuallyUseTime = event.usuallyUseTime, usuallyUseTimeButtonIndex = event.buttonIndex)
                    }
                }

                is OnboardEvent.UpdateProblems -> {
                    updateState {
                        copy(problems = event.problems, problemsButtonIndex = event.buttonIndices)
                    }
                }

                is OnboardEvent.UpdatePeriod -> {
                    updateState {
                        copy(period = event.period)
                    }
                }

                is OnboardEvent.UpdateScreenGoalTime -> {
                    updateState {
                        copy(screenGoalTime = event.screeGoalTime)
                    }
                }

                is OnboardEvent.AddApps -> {
                    updateState {
                        copy(appCodeList = appCodeList + event.appCode)
                    }
                }

                is OnboardEvent.DeleteApp -> {
                    updateState {
                        copy(appCodeList = appCodeList - event.appCode)
                    }
                }

                is OnboardEvent.UpdateAppGoalTimeMinute -> {
                    updateState {
                        copy(appGoalTimeMinute = event.goalTimeMinute)
                    }
                }

                is OnboardEvent.UpdateAppGoalTimeHour -> {
                    updateState {
                        copy(appGoalTimeHour = event.goalTimeHour)
                    }
                }

                is OnboardEvent.UpdateNextButtonActive -> {
                    updateState {
                        copy(isNextButtonActive = event.isNextButtonActive)
                    }
                }

                is OnboardEvent.UpdateAccessToken -> {
                    updateState {
                        copy(accessToken = event.accessToken)
                    }
                }

                is OnboardEvent.ChangeActivityButtonText -> {
                    updateState {
                        copy(buttonText = event.buttonText)
                    }
                }

                is OnboardEvent.VisibleProgressbar -> {
                    updateState {
                        copy(progressbarVisible = event.progressbarVisible)
                    }
                }

                is OnboardEvent.UpdateBackButtonActive -> {
                    updateState {
                        copy(isBackButtonActive = event.isBackButtonActive)
                    }
                }
            }
        }

        fun signUp() {
            viewModelScope.launch {
                val state = onBoardingState.value
                val token = state.accessToken
                val request = getRequestDomain(state)
                authRepository
                    .signUp(token, request)
                    .onSuccess { signUpUser ->
                        signUpUser.let {
                            hmhNetworkPreference.accessToken = it.accessToken
                            hmhNetworkPreference.refreshToken = it.refreshToken
                            hmhNetworkPreference.userId = it.userId
                            hmhNetworkPreference.autoLoginConfigured = true
                        }
                        viewModelScope.launch {
                            _onboardEffect.emit(OnboardEffect.OnboardSuccess)
                            AmplitudeUtils.trackEventWithProperties("complete_onboarding_finish")
                        }
                    }.onFailure {
                        viewModelScope.launch {
                            _onboardEffect.emit(OnboardEffect.OnboardFail)
                        }
                    }
            }
        }

        private fun getRequestDomain(state: OnBoardingState) =
            SignRequestDomain(
                challenge =
                    SignRequestDomain.Challenge(
                        period = state.period,
                        app =
                            state.appCodeList.map { appCode ->
                                SignRequestDomain.Challenge.App(
                                    appCode = appCode,
                                    goalTime = state.appGoalTime,
                                )
                            },
                        goalTime = state.goalTime,
                    ),
                onboarding =
                    SignRequestDomain.Onboarding(
                        averageUseTime = state.usuallyUseTime,
                        problem = state.problems,
                    ),
            )
    }
