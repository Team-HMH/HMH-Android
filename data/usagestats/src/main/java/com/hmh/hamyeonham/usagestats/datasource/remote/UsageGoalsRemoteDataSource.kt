package com.hmh.hamyeonham.usagestats.datasource.remote

import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.network.usagegoal.DailyChallengeService
import com.hmh.hamyeonham.usagestats.mapper.toTotalUsageGoal
import com.hmh.hamyeonham.usagestats.mapper.toUsageGoalList
import javax.inject.Inject

class UsageGoalsRemoteDataSource @Inject constructor(
    private val dailyChallengeService: DailyChallengeService,
) {

    suspend fun getUsageGoals(): Result<List<UsageGoal>> {
        return runCatching { dailyChallengeService.getUsageGoal().data.toUsageGoalList() }
    }
    suspend fun getTotalUsageGoal(): Result<UsageGoal> {
        return runCatching { dailyChallengeService.getUsageGoal().data.toTotalUsageGoal()}
    }
}
