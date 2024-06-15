package com.hmh.hamyeonham.core.network.lock.api

import com.hmh.hamyeonham.core.network.lock.model.LockCheckWithDate
import com.hmh.hamyeonham.core.network.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LockService {
    @GET("/api/v1/user/daily/lock")
    fun getIsLockedWithDate(
        @Query("lockCheckDate") date: String
    ):BaseResponse<LockCheckWithDate>

    @POST("/api/v1/user/daily/lock")
    fun postLockWithDate(
        @Body lockCheckWithDate: LockCheckWithDate
    ):BaseResponse<Unit>
}