package com.hmh.hamyeonham.challenge.appadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.appadd.appselection.AppSelectionModel
import com.hmh.hamyeonham.challenge.usecase.GetInstalledAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AppAddEffect {}

data class AppAddState(
    val installedApp: List<String> = emptyList(),
    val selectedApps: List<String> = emptyList(),
    val goalHour: Long = 0,
    val goalMin: Long = 0,
) {
    val goalTime = goalHour + goalMin
    val appSelectionList = installedApp.map { AppSelectionModel(it, selectedApps.contains(it)) }
}

@HiltViewModel
class AppAddViewModel @Inject constructor(
    private val getInstalledAppUseCase: GetInstalledAppUseCase
) : ViewModel() {
    private fun getInstalledApps() {
        viewModelScope.launch {
            delay(50)
            val installApps = getInstalledAppUseCase()
            updateState {
                copy(installedApp = installApps)
            }
        }
    }

    init {
        getInstalledApps()
    }

    private val _state = MutableStateFlow(AppAddState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AppAddEffect>(1)
    val effect = _effect.asSharedFlow()

    private fun updateState(transform: AppAddState.() -> AppAddState) {
        val currentState = state.value
        val newState = currentState.transform()
        _state.value = newState
    }

    fun checkApp(packageName: String) {
        updateState {
            copy(selectedApps = selectedApps + packageName)
        }

    }

    fun unCheckApp(packageName: String) {
        updateState {
            copy(selectedApps = selectedApps - packageName)
        }
    }

    fun setGoalHour(goalHour: Long) {
        updateState {
            copy(goalHour = goalHour)
        }
    }

    fun setGoalMin(goalMin: Long) {
        updateState {
            copy(goalMin = goalMin)
        }
    }


}
