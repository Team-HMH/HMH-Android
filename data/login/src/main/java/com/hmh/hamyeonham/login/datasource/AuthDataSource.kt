package com.hmh.hamyeonham.login.datasource

import com.kakao.sdk.user.model.User

interface AuthDataSource {
    suspend fun login(): Result<String>
    suspend fun fetchUserProfile(): Result<User>
    suspend fun logout(): Result<Unit>
}
