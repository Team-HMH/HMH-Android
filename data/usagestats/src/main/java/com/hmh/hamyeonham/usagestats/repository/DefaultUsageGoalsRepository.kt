package com.hmh.hamyeonham.usagestats.repository

import com.hmh.hamyeonham.core.database.dao.UsageGoalsDao
import com.hmh.hamyeonham.core.database.dao.UsageTotalGoalDao
import com.hmh.hamyeonham.core.domain.usagegoal.model.AppUsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.datasource.local.UsageGoalsLocalDataSource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class DefaultUsageGoalsRepository @Inject constructor(
    private val usageGoalsLocalDataSource: UsageGoalsLocalDataSource,
    private val usageGoalsDao: UsageGoalsDao,
    private val usageTotalGoalDao: UsageTotalGoalDao,
) : UsageGoalsRepository {

    override suspend fun updateUsageGoal(): Result<Boolean> {
        return runCatching {
//            usageGoalsRemoteDataSource.getUsageGoals().fold(
//                onSuccess = { usageGoals ->
//                    val totalGoalTime = usageGoals.totalGoalTime
//                    usageTotalGoalDao.insertUsageTotalGoal(
//                        UsageTotalGoalEntity(
//                            totalGoalTime = totalGoalTime,
//                            status = usageGoals.status.name
//                        )
//                    )
//
//                    // 각 앱 목표 시간 저장
//                    usageGoalsDao.insertUsageGoalList(usageGoals.toUsageGoalEntityList())
//                    usageGoals.status.name != ChallengeStatus.FAILURE.name
//                },
//                onFailure = {
//                    false
//                }
//            )
            Timber.d("Updating usage goals in local data source")
            return Result.success(true)
        }
    }

    override suspend fun getUsageGoals(): Flow<AppUsageGoal> {
        return usageGoalsLocalDataSource.getUsageGoal()
    }

    override suspend fun addUsageGoal(appUsageGoal: AppUsageGoal.App) {
        usageGoalsLocalDataSource.addUsageAppGoal(appUsageGoal)
    }

    override suspend fun addUsageGoalList(appUsageGoalList: List<AppUsageGoal.App>) {
        usageGoalsLocalDataSource.addUsageGoalList(appUsageGoalList)
    }

    override suspend fun deleteUsageGoal(packageName: String) {
        usageGoalsLocalDataSource.deleteUsageGoal(packageName)
    }
}
