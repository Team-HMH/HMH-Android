package com.hmh.hamyeonham.usagestats.model

import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal

data class UsageStatusAndGoal(
    val totalTimeInForeground: Long = 0,
    val totalGoalTime: Long = 0,
    val apps: List<App> = emptyList()
) {
    data class App(
        val packageName: String = "",
        val usageTime: Long = 0,
        val goalTime: Long = 0,
    ) {
        val timeLeftInMinute = msToMinute(if(goalTime > usageTime) goalTime - usageTime else 0)
        val goalTimeInMinute = msToMinute(goalTime)

        private fun msToMinute(time: Long): Long {
            return if (time < 0) {
                0
            } else {
                time / 1000 / 60
            }
        }

        val usedPercentage: Int = if (goalTime == 0L || usageTime > goalTime) {
            100
        } else {
            ((usageTime * 100 / goalTime).toInt())
        }

        companion object {
            fun List<UsageGoal.App>.toApps(): List<App> {
                return map {
                    App(
                        packageName = it.packageName,
                        goalTime = it.goalTime,
                    )
                }
            }
        }
    }
}
