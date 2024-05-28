package com.hmh.hamyeonham.domain.point.model

data class PointInfo(
    val period: Int = 0,
    val currentUserPoint: Int = 0,
    val challengePointStatuses: List<ChallengePointStatus> = emptyList(),
    val challengePoint: Int = 20,
) {
    data class ChallengePointStatus(
        val challengeDate: String = "",
        val status: GetPointStatus = GetPointStatus.UNEARNED,
    )

    enum class GetPointStatus {
        UNEARNED,
        EARNED,
        FAILURE,
        NONE
    }
}
