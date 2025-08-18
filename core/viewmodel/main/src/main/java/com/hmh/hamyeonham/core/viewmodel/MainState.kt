package com.hmh.hamyeonham.core.viewmodel

import com.hmh.hamyeonham.challenge.model.AppGoal
import com.hmh.hamyeonham.core.domain.usagegoal.model.AppUsageGoal

data class MainState(
    val appGoals: List<AppGoal> = emptyList(),
    val appUsageGoals: AppUsageGoal = AppUsageGoal(),
    val name: String = "",
    var permissionGranted: Boolean = true,
)