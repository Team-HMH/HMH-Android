package com.hmh.hamyeonham.usagestats.repository

import android.util.Log
import com.hmh.hamyeonham.usagestats.datasource.UsageStatsDataSource
import com.hmh.hamyeonham.usagestats.model.UsageStat
import com.hmh.hamyeonham.usagestats.model.UsageStatModel
import javax.inject.Inject

class DefaultUsageStatsRepository
    @Inject
    constructor(
        private val usageStatsDataSource: UsageStatsDataSource,
    ) : UsageStatsRepository {
        override fun getUsageStats(
            startTime: Long,
            endTime: Long,
        ): List<UsageStat> {
            val usageStatsList = usageStatsDataSource.getUsageStats(startTime, endTime)

            printList(usageStatsList)
            return usageStatsList.map { usageStatModel ->
                UsageStat(usageStatModel.packageName, usageStatModel.totalTimeInForeground)
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

        override fun getUsageTimeForPackages(
            startTime: Long,
            endTime: Long,
            vararg packageNames: String,
        ): List<UsageStat> {
            val usageStatsList = getUsageStats(startTime, endTime)
            val newUsageStatList: List<UsageStat> =
                packageNames.map { packageName ->
                    UsageStat(
                        packageName,
                        usageStatsList.filter { it.packageName == packageName }
                            .sumOf { it.totalTimeInForeground },
                    )
                }
            return newUsageStatList
        }

        override fun getUsageTimeForPackages(
            startTime: Long,
            endTime: Long,
            packageNames: List<String>,
        ): List<UsageStat> {
            val usageStatsList = getUsageStats(startTime, endTime)
            val newUsageStatList: List<UsageStat> =
                packageNames.map { packageName ->
                    UsageStat(
                        packageName,
                        usageStatsList.filter { it.packageName == packageName }
                            .sumOf { it.totalTimeInForeground },
                    )
                }
            return newUsageStatList
        }

        private fun printList(list: List<UsageStatModel>) {
            for (i in list) {
                Log.d("package", i.packageName)
            }
        }
    }
