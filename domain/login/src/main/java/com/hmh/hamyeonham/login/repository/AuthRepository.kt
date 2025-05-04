package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.model.Login

interface AuthRepository {
    suspend fun login(provider: AuthProvider): Result<Login>
    suspend fun logout(provider: AuthProvider): Result<Unit>
    suspend fun withdrawal(provider: AuthProvider): Result<Unit>
}
