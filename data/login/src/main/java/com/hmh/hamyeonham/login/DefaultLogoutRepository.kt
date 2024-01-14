package com.hmh.hamyeonham.login

import com.hmh.hamyeonham.core.network.login.LoginService
import com.hmh.hamyeonham.login.repository.LogoutRepository
import javax.inject.Inject

class DefaultLogoutRepository @Inject constructor(
    private val logoutService: LoginService,
) : LogoutRepository {

    override suspend fun logout(accessToken: String): Result<Unit> {
        val bearerToken = "Bearer $accessToken"
        return runCatching {
            logoutService.logout(bearerToken)
        }
    }
}
