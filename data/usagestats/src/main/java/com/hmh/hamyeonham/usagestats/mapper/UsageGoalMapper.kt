package com.hmh.hamyeonham.usagestats.mapper

import com.hmh.hamyeonham.core.database.model.UsageGoalsEntity
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.network.usagegoal.model.UsageGoalResponse

private const val TOTAL = "total"

internal fun UsageGoalResponse.toUsageGoalList(): List<UsageGoal> {
    return listOf(UsageGoal(TOTAL, goalTime ?: 0)) + (apps?.map {
        UsageGoal(it.appCode.orEmpty(), it.goalTime ?: 0)
    } ?: emptyList())
}

internal fun UsageGoalsEntity.toUsageGoal() = UsageGoal(packageName, goalTime)

internal fun UsageGoal.toUsageGoalEntity() = UsageGoalsEntity(packageName, goalTime)

internal fun List<UsageGoal>.toUsageGoalEntityList() = map { it.toUsageGoalEntity() }
