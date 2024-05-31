package com.hmh.hamyeonham.core.network.point.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EarnPointResponse(
    @SerialName("point")
    val point: Int? = 0
)