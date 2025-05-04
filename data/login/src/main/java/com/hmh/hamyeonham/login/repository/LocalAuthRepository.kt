package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.core.database.manger.DatabaseManager
import com.hmh.hamyeonham.core.network.auth.datastore.network.UserPreference
import com.hmh.hamyeonham.login.di.AuthProvider
import com.hmh.hamyeonham.login.model.Login
import javax.inject.Inject

class LocalAuthRepository @Inject constructor(
    private val socialAuthDataStore: Map<AuthProvider, @JvmSuppressWildcards SocialAuthDataStore>,
    private val userPreference: UserPreference,
    private val db: DatabaseManager,
) : AuthRepository {

    override suspend fun login(provider: AuthProvider): Result<Login> = runCatching {
        val socialAuthDataSore = getSocialAuthDataStore(provider)
        val accessToke = socialAuthDataSore.login().getOrThrow()
        val user = socialAuthDataSore.fetchUserProfile().getOrThrow()
        userPreference.apply {
            this.accessToken = accessToke
            this.userId = user.id ?: -1
            this.autoLoginConfigured = true
        }

        Login(
            userId = user.id ?: -1,
            accessToken = accessToke,
            refreshToken = ""
        )
    }

    override suspend fun logout(provider: AuthProvider): Result<Unit> = runCatching {
        val socialAuthDataStore = getSocialAuthDataStore(provider)
        socialAuthDataStore.logout().getOrThrow()
        userPreference.clear()
        db.deleteAll()
    }

    override suspend fun withdrawal(provider: AuthProvider): Result<Unit> = logout(provider)

    private fun getSocialAuthDataStore(authProvider: AuthProvider): SocialAuthDataStore {
        return socialAuthDataStore[authProvider]
            ?: throw IllegalArgumentException("Unsupported auth provider: $authProvider")
    }
}