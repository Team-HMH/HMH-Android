package com.hmh.hamyeonham.data.point.repository

import com.hmh.hamyeonham.common.time.getCurrentDateOfDefaultTimeZone
import com.hmh.hamyeonham.core.network.point.PointService
import com.hmh.hamyeonham.data.point.toEarnPoint
import com.hmh.hamyeonham.data.point.toUsePoint
import com.hmh.hamyeonham.domain.point.model.EarnPoint
import com.hmh.hamyeonham.domain.point.model.UsablePoint
import com.hmh.hamyeonham.domain.point.model.UsePoint
import com.hmh.hamyeonham.domain.point.repository.PointRepository
import javax.inject.Inject

class DefaultPointRepository @Inject constructor(
    private val pointService: PointService
) : PointRepository {
    override suspend fun earnPoint(): Result<EarnPoint> = runCatching {
        val challengeDate = getCurrentDateOfDefaultTimeZone().toString()
        pointService.earnPoint(challengeDate).toEarnPoint()
    }

    override suspend fun getUsablePoint(): Result<UsablePoint> = runCatching {
        pointService.getUsablePoint().toUsePoint()
    }

    override suspend fun usePoint(): Result<UsePoint> {
        return runCatching {
            val challengeDate = getCurrentDateOfDefaultTimeZone().toString()
            pointService.patchPoint(challengeDate).toUsePoint()
        }
    }
}