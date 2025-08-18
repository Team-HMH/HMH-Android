package com.hmh.hamyeonham.core.domain.usagegoal.repository

import com.hmh.hamyeonham.core.domain.usagegoal.model.AppUsageGoal
import kotlinx.coroutines.flow.Flow

interface UsageGoalsRepository {
    suspend fun updateUsageGoal(): Result<Boolean>
    suspend fun getUsageGoals(): Flow<AppUsageGoal>
    suspend fun addUsageGoal(appUsageGoal: AppUsageGoal.App)
    suspend fun addUsageGoalList(appUsageGoalList: List<AppUsageGoal.App>)
    suspend fun deleteUsageGoal(packageName: String)
}
