package com.hmh.hamyeonham.core.network.point

import com.hmh.hamyeonham.core.network.point.model.EarnPointResponse
import com.hmh.hamyeonham.core.network.point.model.UsablePointResponse
import com.hmh.hamyeonham.core.network.point.model.UsePointResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH

interface PointService {
    @FormUrlEncoded
    @PATCH("/api/v1/point/earn")
    suspend fun earnPoint(
        @Field("challengeDate") challengeDate: String
    ): EarnPointResponse

    @GET("/api/v1/point/use")
    suspend fun getUsablePoint(): UsablePointResponse

    @FormUrlEncoded
    @PATCH("/api/v1/point/use")
    suspend fun patchPoint(
        @Field("challengeDate") challengeDate: String
    ): UsePointResponse

}