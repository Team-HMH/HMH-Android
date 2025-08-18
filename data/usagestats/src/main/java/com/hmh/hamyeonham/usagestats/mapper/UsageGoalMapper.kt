package com.hmh.hamyeonham.usagestats.mapper

import com.hmh.hamyeonham.core.database.model.UsageGoalEntity
import com.hmh.hamyeonham.core.domain.usagegoal.model.AppUsageGoal

internal fun UsageGoalEntity.toUsageAppGoal() = AppUsageGoal.App(packageName, goalTime)

internal fun AppUsageGoal.App.toUsageGoalEntity() = UsageGoalEntity(packageName, goalTime)

internal fun AppUsageGoal.toUsageGoalEntityList() = appGoals.map { it.toUsageGoalEntity() }
