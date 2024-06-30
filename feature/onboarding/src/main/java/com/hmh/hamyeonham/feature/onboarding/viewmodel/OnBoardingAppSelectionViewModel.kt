package com.hmh.hamyeonham.feature.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.challenge.usecase.GetInstalledAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class OnBoardingAppSelectionViewModel @Inject constructor(
    private val getInstalledAppUseCase: GetInstalledAppUseCase
) : ViewModel() {
    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps = _installedApps.asStateFlow()

    private val _query = MutableStateFlow("")

    init {
        getInstalledApps()
        setupSearchDebounce()
    }

    private fun getInstalledApps() {
        viewModelScope.launch {
            _installedApps.value = getInstalledAppUseCase()
        }
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
        val searchedApps = installedApps.value.filter { it.appName.contains(query) }
        updateInstalledApps(searchedApps)
    }

    private fun updateInstalledApps(installApps: List<AppInfo>) {
        _installedApps.value = installApps
    }

}
