package com.hmh.hamyeonham.login.usecase

import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.model.Login
import com.hmh.hamyeonham.login.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend fun login(provider: AuthProvider): Result<Login> {
        return authRepository.login(provider)
    }

    suspend fun logout(provider: AuthProvider): Result<Unit> {
        return authRepository.logout(provider)
    }

    suspend fun withdrawal(provider: AuthProvider): Result<Unit> {
        return authRepository.withdrawal(provider)
    }
}
