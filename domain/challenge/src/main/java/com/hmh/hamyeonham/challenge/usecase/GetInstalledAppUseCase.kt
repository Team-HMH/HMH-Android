package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetInstalledAppUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val goalsRepository: UsageGoalsRepository
) {
    suspend operator fun invoke(): List<String> {
        val installedAppList = deviceRepository.getInstalledApps()
        val usageGoals = goalsRepository.getUsageGoals().firstOrNull()

        val installedPackages = installedAppList.toSet()
        val goalPackages = usageGoals?.map { it.packageName }?.toSet() ?: emptySet()

        return (installedPackages - goalPackages).toList()
    }
}
