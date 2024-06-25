package com.hmh.hamyeonham.core.network.login.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("socialPlatform")
    val socialPlatform: String,
)
