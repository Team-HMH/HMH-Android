package com.hmh.hamyeonham.usagestats.datasource.local

import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.core.database.model.UsageGoalsEntity
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.usagestats.mapper.toUsageGoal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsageGoalsLocalDataSource @Inject constructor(
    private val usageGoalsDao: UsageGoalsDao,
    private val usageTotalGoalDao: UsageTotalGoalDao,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getUsageGoal(): Flow<List<UsageGoal>> {
        return usageGoalsDao.getUsageGoal()
            .flatMapConcat { goalsList ->
                flow {
                    val totalGoalTime = withContext(Dispatchers.IO) {
                        usageTotalGoalDao.getUsageTotalGoal()?.goalTime ?: 0
                    }
                    val totalGoal = UsageGoal(packageName = "total", goalTime = totalGoalTime)
                    val result = (listOf(totalGoal) + goalsList.map { it.toUsageGoal() })
                    emit(result)
                }
            }
    }

    suspend fun getUsageGoal(packageName: String): UsageGoal {
        return usageGoalsDao.getUsageGoal(packageName).toUsageGoal()
    }

    suspend fun addUsageGoal(usageGoal: UsageGoal) {
        usageGoalsDao.insertUsageGoal(
            UsageGoalsEntity(
                usageGoal.packageName,
                usageGoal.goalTime
            )
        )
    }

    suspend fun addUsageGoalList(usageGoalList: List<UsageGoal>) {
        usageGoalsDao.insertUsageGoalList(usageGoalList.map {
            UsageGoalsEntity(
                it.packageName,
                it.goalTime
            )
        })
    }

    suspend fun deleteUsageGoal(packageName: String) {
        usageGoalsDao.deleteByPackageName(packageName)
    }

    suspend fun deleteAllUsageGoals() {
        usageGoalsDao.deleteAll()
        usageTotalGoalDao.deleteAll()
    }
}
