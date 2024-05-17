package com.hmh.hamyeonham.core.network.mypage

import com.hmh.hamyeonham.core.network.model.BaseResponse
import com.hmh.hamyeonham.core.network.mypage.datasource.model.UserInfoResponse
import retrofit2.http.GET

interface  MypageService {
    @GET("api/v1/user")
    suspend fun getUserInfo(): BaseResponse<UserInfoResponse>
}
