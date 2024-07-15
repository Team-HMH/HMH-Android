package com.hmh.hamyeonham.challenge.appadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.challenge.usecase.FetchInstalledAppUseCase
import com.hmh.hamyeonham.challenge.usecase.ObserveInstalledAppUseCase
import com.hmh.hamyeonham.challenge.usecase.SearchInstalledAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AppAddEffect {}


@OptIn(FlowPreview::class)
@HiltViewModel
class AppAddViewModel @Inject constructor(
    private val fetchInstalledAppUseCase: FetchInstalledAppUseCase,
    private val searchInstalledAppUseCase: SearchInstalledAppUseCase,
    private val observeInstalledAppUseCase: ObserveInstalledAppUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AppAddState())
    val state = _state.asStateFlow()

    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps = _installedApps.asStateFlow()

    private val _isNextButtonActive = MutableStateFlow(false)
    val isNextButtonActive = _isNextButtonActive.asStateFlow()

    private val _effect = MutableSharedFlow<AppAddEffect>(1)
    val effect = _effect.asSharedFlow()

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            fetchInstalledAppUseCase()
            collectInstalledApps()
            setupSearchDebounce()
        }
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
        _query.debounce(300)
            .onEach { query ->
                searchInstalledAppUseCase(query)
            }
            .launchIn(viewModelScope)
    }

    private fun collectInstalledApps() {
        observeInstalledAppUseCase().onEach {
            _installedApps.value = it
        }.launchIn(viewModelScope)
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
        val buttonState = state.value.goalTime != 0L
        updateNextButtonActive(buttonState)
    }

    private fun handleNextButtonStateWithAppSelection() {
        val buttonState = state.value.selectedApps.isNotEmpty()
        updateNextButtonActive(buttonState)
    }

    private fun updateState(transform: AppAddState.() -> AppAddState) {
        val currentState = state.value
        val newState = currentState.transform()
        _state.value = newState
    }

    private fun updateNextButtonActive(buttonState: Boolean) {
        _isNextButtonActive.value = buttonState
    }
}
