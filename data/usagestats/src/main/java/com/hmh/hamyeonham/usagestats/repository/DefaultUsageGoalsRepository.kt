package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.usagestats.datasource.local.UsageGoalsLocalDataSource
import com.hmh.hamyeonham.usagestats.datasource.remote.UsageGoalsRemoteDataSource
import com.hmh.hamyeonham.usagestats.mapper.toUsageGoal
import com.hmh.hamyeonham.usagestats.mapper.toUsageGoalEntityList
import com.hmh.hamyeonham.usagestats.model.UsageGoal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultUsageGoalsRepository @Inject constructor(
    private val usageGoalsRemoteDataSource: UsageGoalsRemoteDataSource,
    private val usageGoalsLocalDataSource: UsageGoalsLocalDataSource,
    private val usageGoalsDao: UsageGoalsDao
) : UsageGoalsRepository {

    override suspend fun updateUsageGoal() {
        usageGoalsRemoteDataSource.getUsageGoals().onSuccess {
            usageGoalsDao.insertUsageGoalsList(it.toUsageGoalEntityList())
        }.onFailure {
            usageGoalsDao.deleteAll()
        }
    }

    override suspend fun getUsageGoals(): Flow<List<UsageGoal>> {
        return usageGoalsLocalDataSource.getUsageGoal()
    }

    override fun getUsageGoalTimeFromMockData(packageName: String): Long {
        return usageGoalsDao.getUsageGoal(packageName).goalTime
    }

    override fun addUsageGoal(usageGoal: UsageGoal) {
        usageGoalsRemoteDataSource.addUsageGoal(usageGoal)
    }
}
