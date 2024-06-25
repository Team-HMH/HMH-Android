package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetInstalledAppUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val goalsRepository: UsageGoalsRepository,
) {
    private val excludedAppName = "com.hmh.hamyeonham"
    private var installedAppCache: List<AppInfo>? = null
    private var isCacheValid: Boolean = false

    suspend operator fun invoke(): List<AppInfo> {
        if (!isCacheValid) {
            val installedAppList = deviceRepository.getInstalledApps().filter { (packageName, _) ->
                packageName != excludedAppName
            }
            val usageGoals = goalsRepository.getUsageGoals().firstOrNull()

            val installedAppInfoList = installedAppList.toSet()
            val goalPackages = usageGoals?.map { it.packageName }?.toSet() ?: emptySet()
            val excludeAppInfoList = installedAppInfoList.filter { !goalPackages.contains(it.packageName) }

            installedAppCache = excludeAppInfoList
            isCacheValid = true
        }
        return installedAppCache ?: emptyList()
    }

    fun clearCache() {
        installedAppCache = null
        isCacheValid = false
    }
}
