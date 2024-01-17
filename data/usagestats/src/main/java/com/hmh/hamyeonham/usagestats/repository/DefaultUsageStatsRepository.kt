package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.usagestats.datasource.local.UsageStatusLocalDataSource
import com.hmh.hamyeonham.usagestats.model.UsageStatus
import javax.inject.Inject

class DefaultUsageStatsRepository @Inject constructor(
    private val usageStatusLocalDataSource: UsageStatusLocalDataSource,
) : UsageStatsRepository {
    override fun getUsageStats(
        startTime: Long,
        endTime: Long,
    ): List<UsageStatus> {
        val usageStatsList = usageStatusLocalDataSource.getUsageStats(startTime, endTime)
        return usageStatsList.map { usageStatModel ->
            UsageStatus(usageStatModel.packageName, usageStatModel.totalTimeInForeground)
        }
    }

    override fun getUsageTimeForPackage(
        startTime: Long,
        endTime: Long,
        packageName: String,
    ): Long {
        val usageStatsList = getUsageStats(startTime, endTime)
        return usageStatsList.firstOrNull { it.packageName == packageName }?.totalTimeInForeground
            ?: 0
    }

    override fun getUsageStatForPackages(
        startTime: Long,
        endTime: Long,
        vararg packageNames: String,
    ): List<UsageStatus> {
        val usageStatList = getUsageStats(startTime, endTime)
        return packageNames.map { packageName ->
            UsageStatus(
                packageName,
                usageStatList.find {
                    packageName == it.packageName
                }?.totalTimeInForeground ?: 0,
            )
        }
    }

    override fun getUsageStatForPackages(
        startTime: Long,
        endTime: Long,
        packageNames: List<String>,
    ): List<UsageStatus> {
        val usageStatList = getUsageStats(startTime, endTime)
        return packageNames.map { packageName ->
            UsageStatus(
                packageName,
                usageStatList.find {
                    packageName == it.packageName
                }?.totalTimeInForeground ?: 0,
            )
        }
    }
}
