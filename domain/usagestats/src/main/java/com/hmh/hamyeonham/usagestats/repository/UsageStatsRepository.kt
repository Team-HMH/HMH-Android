package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.usagestats.model.UsageStat

interface UsageStatsRepository {
    fun getUsageStats(
        startTime: Long,
        endTime: Long,
    ): List<UsageStat>

    fun getUsageTimeForPackage(
        startTime: Long,
        endTime: Long,
        packageName: String,
    ): Long

    fun getUsageStatForPackages(
        startTime: Long,
        endTime: Long,
        vararg packageNames: String,
    ): List<UsageStat>

    fun getUsageStatForPackages(
        startTime: Long,
        endTime: Long,
        packageNames: List<String>,
    ): List<UsageStat>
}
