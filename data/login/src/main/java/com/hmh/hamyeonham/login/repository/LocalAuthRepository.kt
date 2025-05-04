package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.core.database.manger.DatabaseManager
import com.hmh.hamyeonham.core.network.auth.datastore.network.UserPreference
import com.hmh.hamyeonham.login.datasource.AuthDataSource
import com.hmh.hamyeonham.login.model.Login
import com.hmh.hamyeonham.login.model.SignRequestDomain
import com.hmh.hamyeonham.login.model.SignUpUser

class LocalAuthRepository(
    private val authDataSource: AuthDataSource,
    private val preference: UserPreference,
    private val db: DatabaseManager,
) : AuthRepository {

    override suspend fun login(): Result<Login> = runCatching {
        val accessToken = authDataSource.login().getOrThrow()
        val kakaoUser = authDataSource.fetchUserProfile().getOrThrow()

        preference.apply {
            this.accessToken = accessToken
            this.userId = kakaoUser.id ?: -1
            this.autoLoginConfigured = true
        }

        Login(
            accessToken = accessToken,
            refreshToken = "",
            userId = kakaoUser.id ?: -1
        )
    }

    override suspend fun signUp(
        accessToken: String,
        signUpRequest: SignRequestDomain
    ): Result<SignUpUser> =
        Result.success(
            SignUpUser(
                userId = preference.userId,
                accessToken = accessToken,
                refreshToken = ""
            )
        )

    override suspend fun logout(accessToken: String): Result<Unit> = runCatching {
        authDataSource.logout().getOrThrow()
        preference.clear()
        db.deleteAll()
    }

    override suspend fun withdrawal(accessToken: String): Result<Unit> = logout(accessToken)
}