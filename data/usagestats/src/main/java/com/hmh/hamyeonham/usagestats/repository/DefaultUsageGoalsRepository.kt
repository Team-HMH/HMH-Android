package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.core.database.model.UsageTotalGoalEntity
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.datasource.local.UsageGoalsLocalDataSource
import com.hmh.hamyeonham.usagestats.datasource.remote.UsageGoalsRemoteDataSource
import com.hmh.hamyeonham.usagestats.mapper.toUsageGoalEntityList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUsageGoalsRepository @Inject constructor(
    private val usageGoalsRemoteDataSource: UsageGoalsRemoteDataSource,
    private val usageGoalsLocalDataSource: UsageGoalsLocalDataSource,
    private val usageGoalsDao: UsageGoalsDao,
    private val usageTotalGoalDao: UsageTotalGoalDao,
) : UsageGoalsRepository {

    override suspend fun updateUsageGoal() {
        usageGoalsRemoteDataSource.getUsageGoals().onSuccess { usageGoals ->
            val totalTime = usageGoals.firstOrNull()?.goalTime ?: 0
            usageGoalsDao.insertUsageGoalList(usageGoals.toUsageGoalEntityList())
            usageTotalGoalDao.insertUsageTotalGoal(UsageTotalGoalEntity(totalGoalTime = totalTime))
        }
    }

    override suspend fun getUsageGoals(): Flow<List<UsageGoal>> {
        return usageGoalsLocalDataSource.getUsageGoal()
    }

    override suspend fun addUsageGoal(usageGoal: UsageGoal) {
        usageGoalsLocalDataSource.addUsageGoal(usageGoal)
    }

    override suspend fun addUsageGoalList(usageGoalList: List<UsageGoal>) {
        usageGoalsLocalDataSource.addUsageGoalList(usageGoalList)
    }

    override suspend fun deleteUsageGoal(packageName: String) {
        usageGoalsLocalDataSource.deleteUsageGoal(packageName)
    }
}
