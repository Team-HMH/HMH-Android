package com.hmh.hamyeonham.core.network.challenge.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeFinishResponse(
    @SerialName("statuses")
    val statuses: List<String>? = null
)