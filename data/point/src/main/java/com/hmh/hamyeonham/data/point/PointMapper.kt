package com.hmh.hamyeonham.data.point

import com.hmh.hamyeonham.core.network.point.model.EarnPointResponse
import com.hmh.hamyeonham.core.network.point.model.PointListResponse
import com.hmh.hamyeonham.core.network.point.model.UsablePointResponse
import com.hmh.hamyeonham.core.network.point.model.UsePointResponse
import com.hmh.hamyeonham.domain.point.model.EarnPoint
import com.hmh.hamyeonham.domain.point.model.PointInfo
import com.hmh.hamyeonham.domain.point.model.UsablePoint
import com.hmh.hamyeonham.domain.point.model.UsePoint

fun EarnPointResponse.toEarnPoint() = EarnPoint(
    totalUserPoint = point ?: 0
)

fun UsablePointResponse.toUsePoint() = UsablePoint(
    point = point ?: 0
)

fun UsePointResponse.toUsePoint() = UsePoint(
    usagePoint = usagePoint ?: 0,
    userPoint = userPoint ?: 0
)

fun PointListResponse.toPointStatusList() = PointInfo(
    period = period ?: 0,
    currentUserPoint = point ?: 0,
    challengePointStatuses = challengePointStatuses.map {
        PointInfo.ChallengePointStatus(
            challengeDate = it.challengeDate ?: "",
            status = when (it.status) {
                "UNEARNED" -> PointInfo.GetPointStatus.UNEARNED
                "EARNED" -> PointInfo.GetPointStatus.EARNED
                "FAILURE" -> PointInfo.GetPointStatus.FAILURE
                else -> PointInfo.GetPointStatus.NONE
            },
            period = this.period ?: 0,
            challengePoint = 20
        )
    }
)