package com.hmh.hamyeonham.challenge.model

data class ChallengeStatus(
    val appGoals: List<AppGoal> = emptyList(),
    val isSuccessList: List<Status> = emptyList(),
    val goalTime: Int = 0,
    val period: Int = 0,
) {
    data class AppGoal(
        val appCode: String,
        val appGoalTime: Long,
    )
}

enum class Status(val value: String) {
    NONE("NONE"),
    UNEARNED("UNEARNED"),
    EARNED("EARNED"),
    FAILURE("FAILURE"),
}
