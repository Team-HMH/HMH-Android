package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.usagestats.repository.UsageStatsRepository
import javax.inject.Inject

class GetUsageStatFromPackageUseCase @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository
) {
    operator fun invoke(
        startTime: Long,
        endTime: Long,
        packageName: String,
    ): Long {
        return usageStatsRepository.getUsageTimeForPackage(startTime, endTime, packageName)
    }
}
