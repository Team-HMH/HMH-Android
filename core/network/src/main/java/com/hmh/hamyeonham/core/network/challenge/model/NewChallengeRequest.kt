package com.hmh.hamyeonham.core.network.challenge.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewChallengeRequest(
    @SerialName("period")
    val period: Int,
    @SerialName("goalTime")
    val goalTime: Long
)

