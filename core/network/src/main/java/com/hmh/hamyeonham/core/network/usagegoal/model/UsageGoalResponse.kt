package com.hmh.hamyeonham.core.network.usagegoal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsageGoalResponse(
    @SerialName("status")
    val accessToken: String,
    @SerialName("goalTime")
    val goalTime: Long,
    @SerialName("apps")
    val apps: List<AppGoal>,
) {
    @Serializable
    data class AppGoal(
        @SerialName("appCode")
        val appCode: String,
        @SerialName("goalTime")
        val goalTime: Long,
    )
}
