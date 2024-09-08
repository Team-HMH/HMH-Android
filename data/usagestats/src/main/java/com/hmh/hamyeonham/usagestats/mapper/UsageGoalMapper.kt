package com.hmh.hamyeonham.usagestats.mapper

import com.hmh.hamyeonham.core.database.model.UsageGoalEntity
import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.network.challenge.model.ChallengeResponse

internal fun ChallengeResponse.toUsageGoalList(): UsageGoal {
    val status =
        if (todayIndex > -1) ChallengeStatus.fromString(statuses[todayIndex]) else ChallengeStatus.NONE
    return UsageGoal(
        status = status,
        totalGoalTime = goalTime,
        appGoals = apps.map { it.toApp() }
    )
}

internal fun ChallengeResponse.AppGoal.toApp() = UsageGoal.App(appCode, goalTime)

internal fun UsageGoalEntity.toUsageAppGoal() = UsageGoal.App(packageName, goalTime)

internal fun UsageGoal.App.toUsageGoalEntity() = UsageGoalEntity(packageName, goalTime)

internal fun UsageGoal.toUsageGoalEntityList() = appGoals.map { it.toUsageGoalEntity() }
