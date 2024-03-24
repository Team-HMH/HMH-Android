package com.hmh.hamyeonham.challenge.model

data class ChallengeStatus(
    val appGoals: List<AppGoal> = emptyList(),
    val challengeStatusList: List<Status> = emptyList(),
    val goalTime: Long = 0,
    val period: Int = 0,
    val todayIndex: Int = 0,
    val challengeSuccess: Boolean = true,
) {
    data class AppGoal(
        val appCode: String,
        val appGoalTime: Long,
    )

    val goalTimeInHours: Int
        get() = (goalTime / 1000 / 60 / 60).toInt()

    enum class Status(val value: String) {
        NONE("NONE"),
        UNEARNED("UNEARNED"),
        EARNED("EARNED"),
        FAILURE("FAILURE"),
        TODAY("TODAY")
    }
}
