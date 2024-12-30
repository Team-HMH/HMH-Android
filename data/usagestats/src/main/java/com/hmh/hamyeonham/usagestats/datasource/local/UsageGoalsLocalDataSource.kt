package com.hmh.hamyeonham.usagestats.datasource.local

import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.core.database.model.UsageGoalEntity
import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.usagestats.mapper.toUsageAppGoal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsageGoalsLocalDataSource @Inject constructor(
    private val usageGoalsDao: UsageGoalsDao,
    private val usageTotalGoalDao: UsageTotalGoalDao,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getUsageGoal(): Flow<UsageGoal> {
        return usageGoalsDao.getUsageGoal()
            .flatMapConcat { goalsList ->
                flow {
                    val totalGoal = usageTotalGoalDao.getUsageTotalGoal()
                    val result = UsageGoal(
                        totalGoalTime = totalGoal?.totalGoalTime ?: 0,
                        status = ChallengeStatus.fromString(totalGoal?.status.orEmpty()),
                        appGoals = goalsList.map { it.toUsageAppGoal() }
                    )
                    emit(result)
                }
            }
    }

    suspend fun addUsageAppGoal(usageAppGoal: UsageGoal.App) {
        usageGoalsDao.insertUsageGoal(
            UsageGoalEntity(
                usageAppGoal.packageName,
                usageAppGoal.goalTime
            )
        )
    }

    suspend fun addUsageGoalList(usageAppGoalList: List<UsageGoal.App>) {
        usageGoalsDao.insertUsageGoalList(usageAppGoalList.map {
            UsageGoalEntity(
                it.packageName,
                it.goalTime
            )
        })
    }

    suspend fun deleteUsageGoal(packageName: String) {
        usageGoalsDao.deleteByPackageName(packageName)
    }
}
