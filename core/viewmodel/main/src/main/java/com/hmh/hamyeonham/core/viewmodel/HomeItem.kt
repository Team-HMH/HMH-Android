package com.hmh.hamyeonham.core.viewmodel

import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal

sealed class HomeItem {
    data class TotalModel(
        val userName: String,
        val challengeSuccess: Boolean,
        val totalGoalTime: Long,
        val totalTimeInForeground: Long,
    ) : HomeItem() {
        val totalPercentage = if (totalGoalTime == 0L || totalTimeInForeground > totalGoalTime) {
            100
        } else {
            ((totalTimeInForeground * 100 / totalGoalTime).toInt())
        }
    }

    data class UsageStaticsModel(
        val usageAppStatusAndGoal: UsageStatusAndGoal.App,
    ) : HomeItem()
}