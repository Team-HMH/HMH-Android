package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import javax.inject.Inject

class SearchInstalledAppUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) {


    suspend operator fun invoke(query: String) {
        deviceRepository.searchApp(query)
    }
}
