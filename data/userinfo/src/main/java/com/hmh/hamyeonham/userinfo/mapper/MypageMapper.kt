package com.hmh.hamyeonham.userinfo.mapper

import com.hmh.hamyeonham.core.network.mypage.datasource.model.UserInfoResponse
import com.hmh.hamyeonham.userinfo.model.UserInfo

internal fun UserInfoResponse.toUserInfo(): UserInfo {
    return UserInfo(
        name = name,
    )
}
