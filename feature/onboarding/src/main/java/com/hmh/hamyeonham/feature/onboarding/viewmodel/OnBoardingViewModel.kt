package com.hmh.hamyeonham.feature.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import com.hmh.hamyeonham.feature.onboarding.model.OnboardingAnswer
import com.hmh.hamyeonham.feature.onboarding.model.OnboardingPageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

sealed interface OnBoardingEffect {}

data class OnBoardingState(
    val onBoardingAnswer: OnboardingAnswer = OnboardingAnswer(),
    val pageInfo: List<OnboardingPageInfo> = emptyList(),
    val isNextButtonActive: Boolean = false,
)

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {

    private val _userResponses = MutableStateFlow(OnboardingAnswer())
    val userResponses = _userResponses.asStateFlow()

    private val _onBoardingState = MutableStateFlow(OnBoardingState())
    val onBoardingState = _onBoardingState.asStateFlow()

    private val _onboardEffect = MutableSharedFlow<OnBoardingEffect>()
    val onboardEffect = _onboardEffect.asSharedFlow()

    init {
        _onBoardingState.value = onBoardingState.value.copy(
            pageInfo = initializeButtonInfoList(),
        )
    }

    fun updateUserResponses(transform: OnboardingAnswer.() -> OnboardingAnswer) {
        val currentState = userResponses.value
        val newState = currentState.transform()
        _userResponses.value = newState
    }

    fun updateState(transform: OnBoardingState.() -> OnBoardingState) {
        val currentState = onBoardingState.value
        val newState = currentState.transform()
        _onBoardingState.value = newState
    }

    private fun initializeButtonInfoList(): List<OnboardingPageInfo> {
        val buttonInfoList = mutableListOf<OnboardingPageInfo>()
        for (index in 0..3) {
            buttonInfoList.add(OnboardingPageInfo(index))
        }
        return buttonInfoList
    }
}
