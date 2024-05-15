package com.hmh.hamyeonham.data.challenge.mapper

import com.hmh.hamyeonham.challenge.model.ChallengeStatus

internal fun List<String>.toStatusList(todayIndex: Int, period: Int): List<ChallengeStatus.Status> {
    // 원본 리스트를 ChallengeStatus.Status 리스트로 변환
    val statusList = this.map {
        when (it) {
            ChallengeStatus.Status.NONE.value -> ChallengeStatus.Status.NONE
            ChallengeStatus.Status.UNEARNED.value -> ChallengeStatus.Status.UNEARNED
            ChallengeStatus.Status.EARNED.value -> ChallengeStatus.Status.EARNED
            ChallengeStatus.Status.FAILURE.value -> ChallengeStatus.Status.FAILURE
            else -> ChallengeStatus.Status.NONE
        }
    }.toMutableList()

    // todayIndex가 유효한 경우
    if (todayIndex > -1) {
        // 필요한 만큼 NONE 상태를 추가
        val additionalItemsCount = period - todayIndex
        val additionalItems = List(additionalItemsCount) { ChallengeStatus.Status.NONE }
        // 추가 항목을 원본 리스트에 덧붙임
        statusList.addAll(additionalItems)
        //today 인덱스에 TODAY 상태를 설정
        statusList[todayIndex] = ChallengeStatus.Status.TODAY
    }
    return statusList
}
