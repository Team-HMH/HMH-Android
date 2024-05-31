package com.hmh.hamyeonham.core.network.point.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsePointResponse(
    @SerialName("usagePoint")
    val usagePoint: Int? = 0,
    @SerialName("userPoint")
    val userPoint: Int? = 0
)