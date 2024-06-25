package com.hmh.hamyeonham.challenge.appadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.challenge.usecase.GetInstalledAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AppAddEffect {}


@OptIn(FlowPreview::class)
@HiltViewModel
class AppAddViewModel @Inject constructor(
    private val getInstalledAppUseCase: GetInstalledAppUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AppAddState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AppAddEffect>(1)
    val effect = _effect.asSharedFlow()

    private val _query = MutableStateFlow("")

    init {
        getInstalledApps()
        setupSearchDebounce()
    }

    override fun onCleared() {
        super.onCleared()
        getInstalledAppUseCase.clearCache()
    }

    private fun updateInstalledApps(installApps: List<AppInfo>) {
        updateState {
            copy(installedApps = installApps)
        }
    }

    private fun updateState(transform: AppAddState.() -> AppAddState) {
        val currentState = state.value
        val newState = currentState.transform()
        _state.value = newState
    }

    fun appCheckChanged(packageName: String, isCheck: Boolean) {
        if (isCheck) {
            checkApp(packageName)
        } else {
            unCheckApp(packageName)
        }
    }

    fun setGoalHour(goalHour: Long) {
        updateState {
            copy(goalHour = goalHour)
        }
        handleNextButtonStateWithGoalTime()
    }

    fun setGoalMin(goalMin: Long) {
        updateState {
            copy(goalMin = goalMin)
        }
        handleNextButtonStateWithGoalTime()
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    private fun setupSearchDebounce() {
        viewModelScope.launch {
            _query.debounce(300)
                .collect { query ->
                    searchApp(query)
                }
        }
    }

    private fun searchApp(query: String) {
        if (query.isEmpty()) {
            getInstalledApps()
            return
        }
        updateState {
            copy(installedApps = state.value.installedApps.filter { it.appName.contains(query) })
        }
    }

    private fun getInstalledApps() {
        viewModelScope.launch {
            val installApps = getInstalledAppUseCase()
            updateInstalledApps(installApps)
        }
    }

    private fun checkApp(packageName: String) {
        updateState {
            copy(selectedApps = selectedApps + packageName)
        }
        handleNextButtonStateWithAppSelection()
    }

    private fun unCheckApp(packageName: String) {
        updateState {
            copy(selectedApps = selectedApps - packageName)
        }
        handleNextButtonStateWithAppSelection()
    }

    fun handleNextButtonStateWithGoalTime() {
        val buttonState = !(state.value.goalTime == 0L)
        updateState {
            copy(isNextButtonActive = buttonState)
        }
    }

    fun handleNextButtonStateWithAppSelection() {
        val buttonState = !(state.value.selectedApps.isEmpty())
        updateState {
            copy(isNextButtonActive = buttonState)
        }
    }
}
