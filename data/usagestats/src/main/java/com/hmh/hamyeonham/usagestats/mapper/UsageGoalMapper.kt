package com.hmh.hamyeonham.usagestats.mapper

import com.hmh.hamyeonham.core.database.model.UsageGoalEntity
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal

internal fun UsageGoalEntity.toUsageAppGoal() = UsageGoal.App(packageName, goalTime)

internal fun UsageGoal.App.toUsageGoalEntity() = UsageGoalEntity(packageName, goalTime)

internal fun UsageGoal.toUsageGoalEntityList() = appGoals.map { it.toUsageGoalEntity() }
