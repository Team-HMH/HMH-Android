package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import javax.inject.Inject

class FetchInstalledAppUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) {

    suspend operator fun invoke() {
        deviceRepository.fetchInstalledApps()
    }
}
