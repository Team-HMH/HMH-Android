package com.hmh.hamyeonham.core.network.usagegoal

import com.hmh.hamyeonham.core.network.model.BaseResponse
import com.hmh.hamyeonham.core.network.usagegoal.model.UsageGoalResponse
import retrofit2.http.GET
import retrofit2.http.PATCH

interface DailyChallengeService {
    @GET("api/v1/challenge/home")
    suspend fun getUsageGoal(): BaseResponse<UsageGoalResponse>

    @PATCH("/api/v1/challene/daily/failure")
    suspend fun updateDailyChallengeFailed(): BaseResponse<Unit>
}
