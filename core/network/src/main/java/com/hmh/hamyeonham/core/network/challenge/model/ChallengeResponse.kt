package com.hmh.hamyeonham.core.network.challenge.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeResponse(
    @SerialName("apps")
    val apps: List<AppGoal>,
    @SerialName("goalTime")
    val goalTime: Long,
    @SerialName("period")
    val period: Int,
    @SerialName("statuses")
    val statuses: List<String>,
    @SerialName("todayIndex")
    val todayIndex: Int,
    @SerialName("startDate")
    val startDate: String
) {
    @Serializable
    data class AppGoal(
        @SerialName("appCode")
        val appCode: String,
        @SerialName("goalTime")
        val goalTime: Long,
    )
}
