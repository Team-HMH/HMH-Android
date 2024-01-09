package com.hmh.hamyeonham.core

import androidx.lifecycle.ViewModel
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class MainState(
    val challengeStatus: ChallengeStatus
)

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _mainState = MutableStateFlow(MainState(ChallengeStatus()))
    val mainState = _mainState.asStateFlow()

    fun setChallengeStatus(challengeStatus: ChallengeStatus) {
        _mainState.value = _mainState.value.copy(challengeStatus = challengeStatus)
    }
}
