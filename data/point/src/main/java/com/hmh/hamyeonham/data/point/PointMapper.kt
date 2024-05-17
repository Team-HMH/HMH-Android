package com.hmh.hamyeonham.data.point

import com.hmh.hamyeonham.core.network.point.model.EarnPointResponse
import com.hmh.hamyeonham.core.network.point.model.UsablePointResponse
import com.hmh.hamyeonham.core.network.point.model.UsePointResponse
import com.hmh.hamyeonham.domain.point.model.EarnPoint
import com.hmh.hamyeonham.domain.point.model.UsablePoint
import com.hmh.hamyeonham.domain.point.model.UsePoint

fun EarnPointResponse.toEarnPoint() = EarnPoint(
    point = point ?: 0
)

fun UsablePointResponse.toUsePoint() = UsablePoint(
    point = point ?: 0
)

fun UsePointResponse.toUsePoint() = UsePoint(
    usagePoint = usagePoint ?: 0,
    userPoint = userPoint ?: 0
)