package com.hmh.hamyeonham.core.network.challenge.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeWithUsageResponse(
    @SerialName("data")
    val `data`: Data? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("status")
    val status: Int? = null
) {
    @Serializable
    data class Data(
        @SerialName("statuses")
        val statuses: List<String?>? = null
    )
}
