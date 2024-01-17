package com.hmh.hamyeonham.usagestats.datasource.local

import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.usagestats.mapper.toUsageGoal
import com.hmh.hamyeonham.usagestats.model.UsageGoal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsageGoalsLocalDataSource @Inject constructor(
    private val usageGoalsDao: UsageGoalsDao,
    private val usageTotalGoalDao: UsageTotalGoalDao,
) {
    suspend fun getUsageGoal(): Flow<List<UsageGoal>> {
        return flow {
            usageGoalsDao.getUsageGoal().collect { goalsList ->
                val totalGoalTime = usageTotalGoalDao.getUsageTotalGoal().goalTime
                val totalGoal = UsageGoal(packageName = "total", goalTime = totalGoalTime)
                val result = (listOf(totalGoal) + goalsList.map { it.toUsageGoal() })
                emit(result)
            }
        }
    }
}
