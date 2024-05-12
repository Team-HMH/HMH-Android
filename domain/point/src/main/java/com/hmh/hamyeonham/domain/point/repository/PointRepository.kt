package com.hmh.hamyeonham.domain.point.repository

import com.hmh.hamyeonham.domain.point.model.EarnPoint
import com.hmh.hamyeonham.domain.point.model.UsablePoint
import com.hmh.hamyeonham.domain.point.model.UsePoint

interface PointRepository {
    suspend fun earnPoint(): Result<EarnPoint>
    suspend fun getUsablePoint(): Result<UsablePoint>
    suspend fun usePoint(): Result<UsePoint>
}