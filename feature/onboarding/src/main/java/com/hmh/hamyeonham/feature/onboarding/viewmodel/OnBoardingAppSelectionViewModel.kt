package com.hmh.hamyeonham.feature.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppSelectionState(
    val selectedApp: List<String> = emptyList(),
)

@HiltViewModel
class OnBoardingAppSelectionViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AppSelectionState())
    val state = _state.asStateFlow()
    fun getInstalledApps(): List<String> {
        return deviceRepository.getInstalledApps()
    }

    fun updateState(transform: suspend AppSelectionState.() -> AppSelectionState) {
        viewModelScope.launch {
            val currentState = state.value
            val newState = currentState.transform()
            _state.value = newState
        }
    }
}
