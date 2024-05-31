package com.hmh.hamyeonham.feature.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingAppSelectionViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val excludedAppName = "com.hmh.hamyeonham"

    fun getInstalledApps(): List<AppInfo> {
        return deviceRepository.getInstalledApps().filter { (packageName, _) ->
            packageName != excludedAppName
        }
    }
}
