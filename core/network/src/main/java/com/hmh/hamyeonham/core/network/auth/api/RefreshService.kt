package com.hmh.hamyeonham.core.network.auth.api

import com.hmh.hamyeonham.core.network.auth.api.model.TokenResponse
import com.hmh.hamyeonham.core.network.model.BaseResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshService {
    @POST("api/v1/user/reissue")
    suspend fun refreshToken(@Header("Authorization") token: String): BaseResponse<TokenResponse>
}
