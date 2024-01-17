package com.hmh.hamyeonham.usagestats.datasource.remote

import com.hmh.hamyeonham.core.network.usagegoal.UsageGoalService
import com.hmh.hamyeonham.usagestats.mapper.toUsageGoalList
import com.hmh.hamyeonham.usagestats.model.UsageGoal
import javax.inject.Inject

class UsageGoalsRemoteDataSource @Inject constructor(
    private val usageGoalService: UsageGoalService,
) {

    suspend fun getUsageGoals(): Result<List<UsageGoal>> {
        return runCatching { usageGoalService.getUsageGoal().data.toUsageGoalList() }
    }

    fun addUsageGoal(usageGoal: UsageGoal) {

    }
}
