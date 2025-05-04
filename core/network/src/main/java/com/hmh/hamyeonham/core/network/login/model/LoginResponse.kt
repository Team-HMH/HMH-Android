package com.hmh.hamyeonham.core.network.login.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("token")
    val token: Token? = null,
    @SerialName("userId")
    val userId: Long? = null
) {
    @Serializable
    data class Token(
        @SerialName("accessToken")
        val accessToken: String? = null,
        @SerialName("refreshToken")
        val refreshToken: String? = null
    )
}
