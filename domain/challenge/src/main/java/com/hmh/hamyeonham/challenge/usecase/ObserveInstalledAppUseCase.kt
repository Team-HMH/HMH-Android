package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import javax.inject.Inject

class ObserveInstalledAppUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) {
    operator fun invoke() = deviceRepository.installedApps
}