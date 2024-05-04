package com.hmh.hamyeonham.core.network.usagegoal

import com.hmh.hamyeonham.core.network.usagegoal.model.ChallengeWithUsageRequest
import com.hmh.hamyeonham.core.network.model.BaseResponse
import com.hmh.hamyeonham.core.network.usagegoal.model.UsageGoalResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface DailyChallengeService {
    @GET("api/v1/challenge/home")
    suspend fun getUsageGoal(): BaseResponse<UsageGoalResponse>

    @PATCH("api/v1/challenge/daily/failure")
    suspend fun updateDailyChallengeFailed(): BaseResponse<Unit>

    @POST("/api/v1/challenge/daily/finish")
    suspend fun postChallengeWithUsage(@Body request: ChallengeWithUsageRequest): BaseResponse<Unit>

}
