package com.hmh.hamyeonham.core.network.main

import com.hmh.hamyeonham.core.network.main.model.BannerResponse
import retrofit2.http.GET

interface MainService {
    @GET("api/v2/banner")
    suspend fun getBanner(): Result<BannerResponse>
}