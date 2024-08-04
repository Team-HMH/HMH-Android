package com.hmh.hamyeonham.login

import com.hmh.hamyeonham.core.network.login.AuthService
import com.hmh.hamyeonham.core.network.login.model.LoginRequest
import com.hmh.hamyeonham.core.network.signup.model.toSignUpRequest
import com.hmh.hamyeonham.login.mapper.toLogin
import com.hmh.hamyeonham.login.model.Login
import com.hmh.hamyeonham.login.model.SignRequestDomain
import com.hmh.hamyeonham.login.model.SignUpUser
import com.hmh.hamyeonham.login.repository.AuthRepository
import kotlinx.datetime.TimeZone
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val authService: AuthService,
) : AuthRepository {

    override suspend fun signUp(
        accessToken: String,
        signUpRequest: SignRequestDomain,
    ): Result<SignUpUser> {
        return runCatching {
            val bearerToken = "Bearer $accessToken"
            authService.signUp(
                bearerToken,
                "Android",
                timeZone = TimeZone.currentSystemDefault().id,
                signUpRequest.toSignUpRequest(),
            ).data.toSignUpUser()
        }
    }

    override suspend fun login(accessToken: String): Result<Login> {
        val request = LoginRequest("KAKAO")
        val bearerToken = "Bearer $accessToken"
        return runCatching {
            authService.login(bearerToken, request).data.toLogin()
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
