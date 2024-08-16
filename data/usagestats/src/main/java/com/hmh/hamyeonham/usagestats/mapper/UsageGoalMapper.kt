package com.hmh.hamyeonham.usagestats.mapper

import com.hmh.hamyeonham.core.database.model.UsageGoalEntity
import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.network.usagegoal.model.UsageGoalResponse

internal fun UsageGoalResponse.toUsageGoalList(): UsageGoal {
    return UsageGoal(
        status = ChallengeStatus.fromString(status.orEmpty()),
        totalGoalTime = goalTime ?: 0,
        appGoals = apps?.map { it.toApp() } ?: emptyList()
    )
}

internal fun UsageGoalResponse.AppGoal.toApp() = UsageGoal.App(appCode.orEmpty(), goalTime ?: 0)

internal fun UsageGoalEntity.toUsageAppGoal() = UsageGoal.App(packageName, goalTime)

internal fun UsageGoal.App.toUsageGoalEntity() = UsageGoalEntity(packageName, goalTime)

internal fun UsageGoal.toUsageGoalEntityList() = appGoals.map { it.toUsageGoalEntity() }
