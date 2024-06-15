package com.hmh.hamyeonham.core.network.point.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ChallengeDateRequest(
    @SerializedName("challengeDate")
    val challengeDate: String
)
