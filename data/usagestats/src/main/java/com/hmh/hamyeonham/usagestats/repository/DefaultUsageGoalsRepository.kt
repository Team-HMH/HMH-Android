package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.core.database.model.UsageTotalGoalEntity
import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.datasource.local.UsageGoalsLocalDataSource
import com.hmh.hamyeonham.usagestats.mapper.toUsageGoalEntityList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUsageGoalsRepository @Inject constructor(
    private val usageGoalsLocalDataSource: UsageGoalsLocalDataSource,
) : UsageGoalsRepository {

    override suspend fun getUsageGoals(): Flow<UsageGoal> {
        return usageGoalsLocalDataSource.getUsageGoal()
    }

    override suspend fun addUsageGoal(usageGoal: UsageGoal.App) {
        usageGoalsLocalDataSource.addUsageAppGoal(usageGoal)
    }

    override suspend fun addUsageGoalList(usageGoalList: List<UsageGoal.App>) {
        usageGoalsLocalDataSource.addUsageGoalList(usageGoalList)
    }

    override suspend fun deleteUsageGoal(packageName: String) {
        usageGoalsLocalDataSource.deleteUsageGoal(packageName)
    }
}
