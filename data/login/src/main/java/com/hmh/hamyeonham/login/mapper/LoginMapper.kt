package com.hmh.hamyeonham.login.mapper

import com.hmh.hamyeonham.core.network.login.model.LoginResponse
import com.hmh.hamyeonham.login.model.Login

internal fun LoginResponse.toLogin(): Login {
    return Login(
        userId = userId ?: -1,
        accessToken = token?.accessToken.orEmpty(),
        refreshToken = token?.refreshToken.orEmpty(),
    )
}


