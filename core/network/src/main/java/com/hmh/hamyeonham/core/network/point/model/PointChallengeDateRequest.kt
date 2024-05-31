package com.hmh.hamyeonham.core.network.point.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PointChallengeDateRequest(
    @SerialName("challengeDate")
    val challengeDate: String,
)
