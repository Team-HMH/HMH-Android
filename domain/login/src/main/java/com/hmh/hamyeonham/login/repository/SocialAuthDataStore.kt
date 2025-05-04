package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.login.model.User

interface SocialAuthDataStore {
    suspend fun login(): Result<String>
    suspend fun fetchUserProfile(): Result<User>
    suspend fun logout(): Result<Unit>
}
