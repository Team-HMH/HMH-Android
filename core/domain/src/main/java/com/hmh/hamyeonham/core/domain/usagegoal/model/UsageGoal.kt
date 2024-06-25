package com.hmh.hamyeonham.core.domain.usagegoal.model

data class UsageGoal(
    val packageName: String = "",
    val goalTime: Long = 0
) {

    val goalTimeInHours: Long
        get() = goalTime / 1000 / 60 / 60
    val goalTimeInMinutes: Long
        get() = goalTime / 1000 / 60 % 60

    val formattedGoalTime: String
        get() {
            val hours = goalTimeInHours
            val minutes = goalTimeInMinutes
            return buildString {
                if (hours > 0) {
                    append("$hours 시간")
                }
                if (minutes > 0 || hours == 0L) {
                    append(" $minutes 분")
                }
            }.trim()
        }
    companion object {
        const val TOTAL = "total"
    }
}
