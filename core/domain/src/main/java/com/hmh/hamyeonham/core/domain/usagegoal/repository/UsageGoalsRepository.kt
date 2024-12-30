package com.hmh.hamyeonham.core.domain.usagegoal.repository

import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import kotlinx.coroutines.flow.Flow

interface UsageGoalsRepository {
    suspend fun updateUsageGoal(): Result<Boolean>
    suspend fun getUsageGoals(): Flow<UsageGoal>
    suspend fun addUsageGoal(usageGoal: UsageGoal.App)
    suspend fun addUsageGoalList(usageGoalList: List<UsageGoal.App>)
    suspend fun deleteUsageGoal(packageName: String)
}
