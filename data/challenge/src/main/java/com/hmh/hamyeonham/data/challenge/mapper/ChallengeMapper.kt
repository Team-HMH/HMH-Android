package com.hmh.hamyeonham.data.challenge.mapper

import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.core.network.challenge.model.ChallengeResponse

internal fun ChallengeResponse.toChallengeStatus(): ChallengeStatus {
    return ChallengeStatus(
        apps.map {
            ChallengeStatus.AppGoal(it.appCode, it.goalTime)
        },
        statuses.toStatusList(todayIndex),
        goalTime,
        period,
        todayIndex,
        if (todayIndex > -1) {
            statuses[todayIndex - 1] != ChallengeStatus.Status.FAILURE.value
        } else false
    )
}