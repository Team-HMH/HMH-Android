package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.core.database.manger.DatabaseManager
import com.hmh.hamyeonham.core.network.auth.datastore.network.UserPreference
import com.hmh.hamyeonham.core.network.login.AuthService
import com.hmh.hamyeonham.login.datasource.AuthDataSource
import com.hmh.hamyeonham.login.di.AuthProvider
import jakarta.inject.Inject

class AuthRepositoryFactory @Inject constructor(
    private val authService: AuthService,
    private val dataSourceMap: Map<AuthProvider, @JvmSuppressWildcards AuthDataSource>,
    private val preference: UserPreference,
    private val db: DatabaseManager
) {
    fun createRemoteRepository(provider: AuthProvider): AuthRepository {
        val dataSource = dataSourceMap[provider]
            ?: throw IllegalArgumentException("Invalid provider")
        return DefaultAuthRepository(authService, dataSource)
    }

    fun createLocalRepository(provider: AuthProvider): AuthRepository {
        val dataSource = dataSourceMap[provider]
            ?: throw IllegalArgumentException("Invalid provider")
        return LocalAuthRepository(dataSource, preference, db)
    }
}