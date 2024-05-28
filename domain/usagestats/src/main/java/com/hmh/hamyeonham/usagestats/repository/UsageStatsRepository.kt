package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.usagestats.model.UsageStatus

interface UsageStatsRepository {

    suspend fun getUsageStats(
        targetDate: String
    ): List<UsageStatus>

    suspend fun getUsageStats(
        startTime: Long,
        endTime: Long,
    ): List<UsageStatus>

    suspend fun getUsageTimeForPackage(
        startTime: Long,
        endTime: Long,
        packageName: String,
    ): Long

    suspend fun getUsageStatForPackages(
        startTime: Long,
        endTime: Long,
        vararg packageNames: String,
    ): List<UsageStatus>

    suspend fun getUsageStatForPackages(
        startTime: Long,
        endTime: Long,
        packageNames: List<String>,
    ): List<UsageStatus>
}
