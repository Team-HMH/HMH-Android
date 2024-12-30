package com.hmh.hamyeonham.data.challenge.mapper

import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus

internal fun List<String>.toStatusList(todayIndex: Int): List<ChallengeStatus> {
    // 원본 리스트를 ChallengeStatus.Status 리스트로 변환
    return this.map {
        when (it) {
            ChallengeStatus.NONE.name -> ChallengeStatus.NONE
            ChallengeStatus.UNEARNED.name -> ChallengeStatus.UNEARNED
            ChallengeStatus.EARNED.name -> ChallengeStatus.EARNED
            ChallengeStatus.FAILURE.name -> ChallengeStatus.FAILURE
            else -> ChallengeStatus.NONE
        }
    }.toMutableList().also {
        // todayIndex가 유효한 경우 today 인덱스에 TODAY 상태를 설정
        if (todayIndex > -1)
            it[todayIndex] = ChallengeStatus.TODAY
    }
}
