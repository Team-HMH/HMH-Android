package com.hmh.hamyeonham.usagestats.datasource.local

import com.hmh.hamyeonham.usagestats.model.UsageStatsModel

interface UsageStatusLocalDataSource {
    fun getUsageStats(startTime: Long, endTime: Long): List<UsageStatsModel>
}
