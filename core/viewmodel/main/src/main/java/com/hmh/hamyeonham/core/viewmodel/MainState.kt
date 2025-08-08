package com.hmh.hamyeonham.core.viewmodel

import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal

data class MainState(
    val totalGoalTimeInHour: Int = 0,
    val todayIndex: Int = 0,
    val usageGoals: UsageGoal = UsageGoal(),
    val name: String = "",
    val challengeSuccess: Boolean = true,
    var permissionGranted: Boolean = true,
)