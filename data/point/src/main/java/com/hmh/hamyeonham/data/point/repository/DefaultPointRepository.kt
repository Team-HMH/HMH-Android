package com.hmh.hamyeonham.data.point.repository

import com.hmh.hamyeonham.common.time.getCurrentDateOfDefaultTimeZone
import com.hmh.hamyeonham.core.network.point.PointService
import com.hmh.hamyeonham.core.network.point.model.PointChallengeDateRequest
import com.hmh.hamyeonham.data.point.toEarnPoint
import com.hmh.hamyeonham.data.point.toPointStatusList
import com.hmh.hamyeonham.data.point.toUsePoint
import com.hmh.hamyeonham.domain.point.model.EarnPoint
import com.hmh.hamyeonham.domain.point.model.PointInfo
import com.hmh.hamyeonham.domain.point.model.UsablePoint
import com.hmh.hamyeonham.domain.point.model.UsePoint
import com.hmh.hamyeonham.domain.point.repository.PointRepository
import javax.inject.Inject

class DefaultPointRepository @Inject constructor(
    private val pointService: PointService
) : PointRepository {
    override suspend fun earnPoint(challengeDate: String): Result<EarnPoint> = runCatching {
        pointService.earnPoint(PointChallengeDateRequest(challengeDate)).data.toEarnPoint()
    }

    override suspend fun getUsablePoint(): Result<UsablePoint> = runCatching {
        pointService.getUsablePoint().toUsePoint()
    }

    override suspend fun usePoint(): Result<UsePoint> {
        return runCatching {
            val challengeDate = getCurrentDateOfDefaultTimeZone().toString()
            pointService.patchPoint(PointChallengeDateRequest(challengeDate)).data.toUsePoint()
        }
    }

    override suspend fun getPointInfoList(): Result<PointInfo> {
        return runCatching {
            pointService.getPointInfoList().data.toPointStatusList()
        }
    }
}