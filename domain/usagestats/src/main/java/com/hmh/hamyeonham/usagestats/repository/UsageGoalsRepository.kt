package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.usagestats.model.UsageGoal
import kotlinx.coroutines.flow.Flow

interface UsageGoalsRepository {
    suspend fun updateUsageGoal()
    fun getUsageGoalTimeFromMockData(packageName: String): Long
    fun addUsageGoal(usageGoal: UsageGoal)
    suspend fun getUsageGoals(): Flow<List<UsageGoal>>
}
