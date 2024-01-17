package com.hmh.hamyeonham.core.network.challenge

import com.hmh.hamyeonham.core.network.challenge.model.AppsRequest
import com.hmh.hamyeonham.core.network.challenge.model.ChallengeResponse
import com.hmh.hamyeonham.core.network.model.BaseResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ChallengeService {
    @GET("api/v1/challenge")
    suspend fun getChallengeData(): BaseResponse<ChallengeResponse>

    @POST("api/v1/app")
    suspend fun postApps(request: AppsRequest): BaseResponse<Unit>

    @DELETE("api/v1/app")
    suspend fun deleteApps(request: AppCodeRequest): BaseResponse<Unit>
}
