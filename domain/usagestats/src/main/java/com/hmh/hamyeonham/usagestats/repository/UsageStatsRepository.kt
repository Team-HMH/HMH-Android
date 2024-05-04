package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.usagestats.model.UsageStatus

interface UsageStatsRepository {

    fun getUsageStats(
        targetDate: String
    ): List<UsageStatus>

    fun getUsageStats(
        startTime: Long,
        endTime: Long,
    ): List<UsageStatus>

    fun getUsageTimeForPackage(
        startTime: Long,
        endTime: Long,
        packageName: String,
    ): Long

    fun getUsageStatForPackages(
        startTime: Long,
        endTime: Long,
        vararg packageNames: String,
    ): List<UsageStatus>

    fun getUsageStatForPackages(
        startTime: Long,
        endTime: Long,
        packageNames: List<String>,
    ): List<UsageStatus>
}
