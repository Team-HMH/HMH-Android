package com.hmh.hamyeonham.usagestats.datasource

import com.hmh.hamyeonham.usagestats.model.UsageStatsModel

interface UsageStatsDataSource {
    fun getUsageStats(startTime: Long, endTime: Long): List<UsageStatsModel>
}
