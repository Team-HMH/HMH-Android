package com.hmh.hamyeonham.usagestats.datasource.remote

import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.network.challenge.ChallengeService
import com.hmh.hamyeonham.usagestats.mapper.toUsageGoalList
import javax.inject.Inject

class UsageGoalsRemoteDataSource @Inject constructor(
    private val challengeService: ChallengeService,
) {

    suspend fun getUsageGoals(): Result<UsageGoal> {
        return runCatching { challengeService.getChallengeData().data.toUsageGoalList() }
    }
}
