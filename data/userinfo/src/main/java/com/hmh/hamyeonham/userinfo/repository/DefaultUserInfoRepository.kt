package com.hmh.hamyeonham.userinfo.repository

import com.hmh.hamyeonham.core.network.call.util.getResult
import com.hmh.hamyeonham.core.network.mypage.MyPageService
import com.hmh.hamyeonham.login.mapper.toUserInfo
import com.hmh.hamyeonham.userinfo.model.UserInfo
import javax.inject.Inject

class DefaultUserInfoRepository @Inject constructor(
    private val myPageService: MyPageService
) : UserInfoRepository {
    override suspend fun getUserInfo(): Result<UserInfo> {
        return myPageService.getUserInfo()
            .getResult()
            .mapCatching { response ->
                response.toUserInfo()
            }
    }
}