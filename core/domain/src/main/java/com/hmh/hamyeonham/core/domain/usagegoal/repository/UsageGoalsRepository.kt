package com.hmh.hamyeonham.core.domain.usagegoal.repository

import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import kotlinx.coroutines.flow.Flow

interface UsageGoalsRepository {
    suspend fun updateUsageGoal()
    suspend fun getUsageGoalTimeFromMockData(packageName: String): Long
    suspend fun getUsageGoals(): Flow<List<UsageGoal>>
    suspend fun addUsageGoal(usageGoal: UsageGoal)
    suspend fun addUsageGoalList(usageGoalList: List<UsageGoal>)
    suspend fun deleteUsageGoal(packageName: String)
}
