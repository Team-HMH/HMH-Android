package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.common.time.getTargetDayStartEndEpochMillis
import com.hmh.hamyeonham.usagestats.datasource.local.UsageStatusLocalDataSource
import com.hmh.hamyeonham.usagestats.model.UsageStatus
import kotlinx.datetime.toLocalDate
import javax.inject.Inject

class DefaultUsageStatsRepository @Inject constructor(
    private val usageStatusLocalDataSource: UsageStatusLocalDataSource,
) : UsageStatsRepository {
    override suspend fun getUsageStats(targetDate: String): List<UsageStatus> {
        val (startTime, endTime) = getTargetDayStartEndEpochMillis(targetDate.toLocalDate())
        val usageStatsList = usageStatusLocalDataSource.getUsageStats(startTime, endTime)
        return usageStatsList.map { usageStatModel ->
            UsageStatus(usageStatModel.packageName, usageStatModel.totalTimeInForeground)
        }
    }

    override suspend fun getUsageStats(
        startTime: Long,
        endTime: Long,
    ): List<UsageStatus> {
        val usageStatsList = usageStatusLocalDataSource.getUsageStats(startTime, endTime)
        return usageStatsList.map { usageStatModel ->
            UsageStatus(usageStatModel.packageName, usageStatModel.totalTimeInForeground)
        }
    }

    override suspend fun getUsageTimeForPackage(
        startTime: Long,
        endTime: Long,
        packageName: String,
    ): Long {
        val usageStatsList = getUsageStats(startTime, endTime)
        return usageStatsList.firstOrNull { it.packageName == packageName }?.totalTimeInForeground
            ?: 0
    }

    override suspend fun getUsageStatForPackages(
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

    override suspend fun getUsageStatForPackages(
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

    override suspend fun getUsageStatForPackageOfToday(packageName: String): Long {
        val (startTime, endTime) = getCurrentDayStartEndEpochMillis()
        return getUsageTimeForPackage(startTime, endTime, packageName)
    }

    override fun getForegroundAppPackageName(): String? {
        return usageStatusLocalDataSource.getForegroundAppPackageName()
    }
}
