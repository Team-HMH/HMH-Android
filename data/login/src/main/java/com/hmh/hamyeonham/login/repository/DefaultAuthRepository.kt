package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.core.network.login.AuthService
import com.hmh.hamyeonham.core.network.login.model.LoginRequest
import com.hmh.hamyeonham.core.network.signup.model.toSignUpRequest
import com.hmh.hamyeonham.login.datasource.AuthDataSource
import com.hmh.hamyeonham.login.mapper.toLogin
import com.hmh.hamyeonham.login.model.Login
import com.hmh.hamyeonham.login.model.SignRequestDomain
import com.hmh.hamyeonham.login.model.SignUpUser
import kotlinx.datetime.TimeZone

class DefaultAuthRepository(
    private val authService: AuthService,
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override suspend fun login(): Result<Login> {
        val accessToken = authDataSource.login().getOrThrow()
        val request = LoginRequest("KAKAO") // 실제 provider 이름 필요
        val bearerToken = "Bearer $accessToken"
        return runCatching {
            authService.login(bearerToken, request).data.toLogin()
        }
    }

    override suspend fun signUp(
        accessToken: String,
        signUpRequest: SignRequestDomain
    ): Result<SignUpUser> {
        val bearerToken = "Bearer $accessToken"
        return runCatching {
            authService.signUp(
                bearerToken,
                "Android",
                timeZone = TimeZone.currentSystemDefault().id,
                signUpRequest.toSignUpRequest(),
            ).data.toSignUpUser()
        }
    }

    override suspend fun logout(accessToken: String): Result<Unit> {
        val bearerToken = "Bearer $accessToken"
        return runCatching {
            authService.logout(bearerToken)
        }
    }

    override suspend fun withdrawal(accessToken: String): Result<Unit> {
        val bearerToken = "Bearer $accessToken"
        return runCatching {
            authService.withdrawal(bearerToken)
        }
    }
}