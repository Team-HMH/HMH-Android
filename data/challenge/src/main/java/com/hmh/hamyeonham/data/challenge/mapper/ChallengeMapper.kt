package com.hmh.hamyeonham.data.challenge.mapper

import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.core.network.challenge.model.ChallengeResponse
import com.hmh.hamyeonham.core.network.usagegoal.model.UsageGoalResponse

internal fun ChallengeResponse.toChallengeStatus(): ChallengeStatus {
    return ChallengeStatus(
        apps.map {
            ChallengeStatus.AppGoal(it.appCode, it.goalTime)
        },
        statuses.toStatusList(todayIndex, period),
        goalTime,
        period,
        todayIndex,
    )
}

internal fun UsageGoalResponse.toChallengeResult(): Boolean {
    return when (status) {
        ChallengeStatus.Status.NONE.value -> true
        //FAIL일 경우
        else -> false
    }
}