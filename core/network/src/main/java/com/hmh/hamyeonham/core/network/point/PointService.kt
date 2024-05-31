package com.hmh.hamyeonham.core.network.point

import com.hmh.hamyeonham.core.network.model.BaseResponse
import com.hmh.hamyeonham.core.network.point.model.EarnPointResponse
import com.hmh.hamyeonham.core.network.point.model.PointChallengeDateRequest
import com.hmh.hamyeonham.core.network.point.model.PointListResponse
import com.hmh.hamyeonham.core.network.point.model.UsablePointResponse
import com.hmh.hamyeonham.core.network.point.model.UsePointResponse
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH

interface PointService {

    @PATCH("/api/v1/point/earn")
    suspend fun earnPoint(
        @Body request: PointChallengeDateRequest,
    ): BaseResponse<EarnPointResponse>

    @GET("/api/v1/point/use")
    suspend fun getUsablePoint(): UsablePointResponse

    @PATCH("/api/v1/point/use")
    suspend fun patchPoint(
        @Body request: PointChallengeDateRequest,
    ): BaseResponse<UsePointResponse>

    @GET("/api/v1/point/list")
    suspend fun getPointInfoList(): BaseResponse<PointListResponse>

}