package com.hmh.hamyeonham.login.usecase

import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.model.Login
import com.hmh.hamyeonham.login.model.SignRequestDomain
import com.hmh.hamyeonham.login.model.SignUpUser
import com.hmh.hamyeonham.login.repository.AuthRepositorySelector
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val selector: AuthRepositorySelector
) {
    suspend fun login(provider: AuthProvider): Result<Login> {
        return selector.getRepository(provider).login()
    }

    suspend fun logout(accessToken: String, provider: AuthProvider): Result<Unit> {
        return selector.getRepository(provider).logout(accessToken)
    }

    suspend fun signUp(
        accessToken: String,
        provider: AuthProvider,
        request: SignRequestDomain
    ): Result<SignUpUser> {
        return selector.getRepository(provider).signUp(accessToken, request)
    }

    suspend fun withdrawal(accessToken: String, provider: AuthProvider): Result<Unit> {
        return selector.getRepository(provider).withdrawal(accessToken)
    }
}
