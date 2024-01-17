package com.hmh.hamyeonham.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengeState(
    val modifierState: ModifierState = ModifierState.DELETE,
)

enum class ModifierState {
    DELETE,
    CANCEL,
}

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
) : ViewModel() {

    private val _challengeState = MutableStateFlow(ChallengeState())
    val challengeState = _challengeState.asStateFlow()

    fun updateState(transform: ChallengeState.() -> ChallengeState) {
        val currentState = challengeState.value
        val newState = currentState.transform()
        _challengeState.value = newState
    }

    fun addApp(apps: Apps) {
        viewModelScope.launch {
            challengeRepository.postApps(apps)
        }
    }

    fun deleteApp(packageName: String) {
        viewModelScope.launch {
            challengeRepository.deleteApps(packageName)
        }
    }
}
