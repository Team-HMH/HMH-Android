package com.hmh.hamyeonham.usagestats.model

data class UsageStatusAndGoal(
    val packageName: String = "",
    val totalTimeInForeground: Long = 0,
    val goalTime: Long = 0,
) {
    private val challengeSuccess: Boolean = (goalTime - totalTimeInForeground) >= 0
    val totalTimeInForegroundInMin = msToMin(totalTimeInForeground)
    val goalTimeInMin = msToMin(goalTime)
    val timeLeftInMin: Long by lazy {
        if (challengeSuccess) (goalTimeInMin - totalTimeInForegroundInMin) else 0L
    }

    private fun msToMin(time: Long) = time / 1000 / 60
    val usedPercentage: Int by lazy {
        if (challengeSuccess) (totalTimeInForeground * 100 / (goalTime + 0.0001)).toInt() else 100
    }

    val blackHoleLevel = if (challengeSuccess) usedPercentage / 25 else 5
}
