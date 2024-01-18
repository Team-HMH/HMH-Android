package com.hmh.hamyeonham.challenge.appadd.appselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.usecase.GetInstalledAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppSelectionState(
    val selectedApp: List<String> = emptyList()
)

@HiltViewModel
class AppSelectionViewModel @Inject constructor(
    private val getInstalledAppUseCase: GetInstalledAppUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AppSelectionState())
    val state = _state.asStateFlow()

    fun getInstalledApps() {
        updateState {
            copy(selectedApp = getInstalledAppUseCase())
        }
    }

    private fun updateState(transform: suspend AppSelectionState.() -> AppSelectionState) {
        viewModelScope.launch {
            val currentState = state.value
            val newState = currentState.transform()
            _state.value = newState
        }
    }
}
