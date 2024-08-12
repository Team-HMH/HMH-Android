package com.hmh.hamyeonham.core.viewmodel

import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal

sealed class HomeItem {
    data class TotalModel(
        val userName: String,
        val challengeSuccess: Boolean,
        val totalGoalTime: Long,
        val totalTimeInForeground: Long,
        val usageAppStatusAndGoal: UsageStatusAndGoal.App,
    ) : HomeItem()

    data class UsageStaticsModel(
        val usageAppStatusAndGoal: UsageStatusAndGoal.App,
    ) : HomeItem()
}