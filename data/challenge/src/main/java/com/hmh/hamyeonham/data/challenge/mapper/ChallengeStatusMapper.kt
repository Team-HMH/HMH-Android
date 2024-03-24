package com.hmh.hamyeonham.data.challenge.mapper

import com.hmh.hamyeonham.challenge.model.ChallengeStatus

internal fun List<String>.toStatusList(todayIndex: Int): List<ChallengeStatus.Status> {
    return this.map {
        when (it) {
            ChallengeStatus.Status.NONE.value -> ChallengeStatus.Status.NONE
            ChallengeStatus.Status.UNEARNED.value -> ChallengeStatus.Status.UNEARNED
            ChallengeStatus.Status.EARNED.value -> ChallengeStatus.Status.EARNED
            ChallengeStatus.Status.FAILURE.value -> ChallengeStatus.Status.FAILURE
            else -> ChallengeStatus.Status.NONE
        }
    }.toMutableList().also {
        if (todayIndex > -1)
            it[todayIndex - 1] = ChallengeStatus.Status.TODAY
    }
}
