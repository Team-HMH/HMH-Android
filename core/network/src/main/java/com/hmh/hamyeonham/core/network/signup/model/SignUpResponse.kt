package com.hmh.hamyeonham.core.network.signup.model

import com.hmh.hamyeonham.core.network.login.model.LoginResponse
import com.hmh.hamyeonham.login.model.SignUpUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    @SerialName("token")
    val token: LoginResponse.Token? = null,
    @SerialName("userId")
    val userId: Int,
) {
    @Serializable
    data class Token(
        @SerialName("accessToken")
        val accessToken: String? = null,
        @SerialName("refreshToken")
        val refreshToken: String? = null,
    )

    fun toSignUpUser(): SignUpUser {
        return SignUpUser(
            userId = userId,
            accessToken = token?.accessToken.orEmpty(),
            refreshToken = token?.refreshToken.orEmpty(),
        )
    }
}
