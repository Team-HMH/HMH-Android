package com.hmh.hamyeonham.usagestats.model

data class UsageStatusAndGoal(
    val totalTimeInForeground: Long = 0,
    val totalGoalTime: Long = 0,
    val apps: List<App> = emptyList()
) {
    data class App(
        val packageName: String = "",
        val usageTime:Long = 0,
        val goalTime: Long = 0,
    )

    private val totalTimeExceededGoalTime: Boolean = (totalGoalTime - totalTimeInForeground) >= 0
    val totalTimeInForegroundInMin = msToMin(totalTimeInForeground)
    val goalTimeInMin = msToMin(totalGoalTime)
    val timeLeftInMin: Long by lazy {
        if (totalTimeExceededGoalTime) (goalTimeInMin - totalTimeInForegroundInMin) else 0L
    }

    private fun msToMin(time: Long) = time / 1000 / 60
    val usedPercentage: Int by lazy {
        if (totalTimeExceededGoalTime) (totalTimeInForeground * 100 / (totalGoalTime + 0.0001)).toInt() else 100
    }
}
