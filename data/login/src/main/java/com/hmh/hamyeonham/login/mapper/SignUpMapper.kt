package com.hmh.hamyeonham.login.mapper

import com.hmh.hamyeonham.core.network.login.model.LoginResponse
import com.hmh.hamyeonham.core.network.signup.model.SignUpResponse

internal fun SignUpResponse.toSignUp(): SignUpResponse {
    return SignUpResponse(
        userId = userId ?: -1,
        token = LoginResponse.Token(
            accessToken = token?.accessToken.orEmpty(),
            refreshToken = token?.refreshToken.orEmpty(),
        ),
    )
}
