package com.hmh.hamyeonham.feature.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.challenge.usecase.FetchInstalledAppUseCase
import com.hmh.hamyeonham.challenge.usecase.ObserveInstalledAppUseCase
import com.hmh.hamyeonham.challenge.usecase.SearchInstalledAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class OnBoardingAppSelectionViewModel @Inject constructor(
    private val fetchInstalledAppUseCase: FetchInstalledAppUseCase,
    private val searchInstalledAppUseCase: SearchInstalledAppUseCase,
    private val observeInstalledAppUseCase: ObserveInstalledAppUseCase
) : ViewModel() {
    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps = _installedApps.asStateFlow()

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            fetchInstalledAppUseCase()
            collectInstalledApps()
            setupSearchDebounce()
        }
    }

    private fun collectInstalledApps() {
        observeInstalledAppUseCase().onEach {
            _installedApps.value = it
        }.launchIn(viewModelScope)
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    private fun setupSearchDebounce() {
        _query.debounce(300)
            .onEach { query ->
                searchInstalledAppUseCase(query)
            }.launchIn(viewModelScope)
    }

}
