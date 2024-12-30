package com.hmh.hamyeonham.core.network.usagegoal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsageGoalResponse(
    @SerialName("status")
    val status: String? = null,
    @SerialName("goalTime")
    val goalTime: Long? = null,
    @SerialName("apps")
    val apps: List<AppGoal>? = null,
) {
    @Serializable
    data class AppGoal(
        @SerialName("appCode")
        val appCode: String? = null,
        @SerialName("goalTime")
        val goalTime: Long? = null,
    )
}
