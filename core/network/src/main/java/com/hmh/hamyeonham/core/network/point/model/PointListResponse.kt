package com.hmh.hamyeonham.core.network.point.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PointListResponse(
    @SerialName("point")
    val point: Int? = 0,
    @SerialName("period")
    val period: Int? = 0,
    @SerialName("challengePointStatuses")
    val challengePointStatuses: List<ChallengePointStatus> = emptyList(),
) {
    @Serializable
    data class ChallengePointStatus(
        @SerialName("challengeDate")
        val challengeDate: String? = "",
        @SerialName("status")
        val status: String? = "NONE"
    )
}