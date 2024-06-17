package com.hmh.hamyeonham.core.network.point

import com.hmh.hamyeonham.core.network.model.BaseResponse
import com.hmh.hamyeonham.core.network.point.model.ChallengeDateRequest
import com.hmh.hamyeonham.core.network.point.model.EarnPointRequest
import com.hmh.hamyeonham.core.network.point.model.EarnPointResponse
import com.hmh.hamyeonham.core.network.point.model.PointListResponse
import com.hmh.hamyeonham.core.network.point.model.UsablePointResponse
import com.hmh.hamyeonham.core.network.point.model.UsePointResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface PointService {

    @PATCH("/api/v1/point/earn")
    suspend fun earnPoint(
        @Body request: EarnPointRequest,
    ): BaseResponse<EarnPointResponse>

    @GET("/api/v1/point/use")
    suspend fun getUsablePoint(): UsablePointResponse

    @PATCH("/api/v1/point/use")
    suspend fun patchPoint(
        @Body body: ChallengeDateRequest
    ): UsePointResponse

    @GET("/api/v1/point/list")
    suspend fun getPointInfoList(): BaseResponse<PointListResponse>

}