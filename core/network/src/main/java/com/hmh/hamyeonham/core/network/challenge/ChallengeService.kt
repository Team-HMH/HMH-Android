package com.hmh.hamyeonham.core.network.challenge

import com.hmh.hamyeonham.core.network.challenge.model.AppsRequest
import com.hmh.hamyeonham.core.network.challenge.model.NewChallengeRequest
import com.hmh.hamyeonham.core.network.challenge.model.ChallengeResponse
import com.hmh.hamyeonham.core.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface ChallengeService {
    @GET("api/v1/challenge")
    suspend fun getChallengeData(): BaseResponse<ChallengeResponse>
    @POST("api/v1/challenge")
    suspend fun postNewChallenge(@Body request: NewChallengeRequest): BaseResponse<Unit>

    @POST("api/v1/challenge/app")
    suspend fun postApps(@Body request: AppsRequest): BaseResponse<Unit>

    @HTTP(method = "DELETE", path = "api/v1/challenge/app", hasBody = true)
    suspend fun deleteApps(@Body request: AppCodeRequest): BaseResponse<Unit>
}
