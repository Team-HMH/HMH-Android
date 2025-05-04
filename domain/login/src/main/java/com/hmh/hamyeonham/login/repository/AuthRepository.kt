package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.model.Login
import com.hmh.hamyeonham.login.model.SignRequestDomain
import com.hmh.hamyeonham.login.model.SignUpUser
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteAuth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalAuth

interface AuthRepository {
    suspend fun login(): Result<Login>
    suspend fun logout(accessToken: String): Result<Unit>
    suspend fun signUp(accessToken: String, signUpRequest: SignRequestDomain): Result<SignUpUser>
    suspend fun withdrawal(accessToken: String): Result<Unit>
}
