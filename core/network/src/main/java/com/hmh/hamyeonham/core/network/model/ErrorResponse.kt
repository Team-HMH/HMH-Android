package com.hmh.hamyeonham.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse<T>(
    @SerialName("status")
    val status: Int,
    @SerialName("message")
    val message: String,
)
