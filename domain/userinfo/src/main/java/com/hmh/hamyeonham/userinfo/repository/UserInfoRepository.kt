package com.hmh.hamyeonham.userinfo.repository

import com.hmh.hamyeonham.userinfo.model.UserInfo

interface UserInfoRepository {
    suspend fun getUserInfo(): Result<UserInfo>
}
