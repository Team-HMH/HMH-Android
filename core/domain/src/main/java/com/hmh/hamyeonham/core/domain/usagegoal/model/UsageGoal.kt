package com.hmh.hamyeonham.core.domain.usagegoal.model

data class UsageGoal(
    val totalGoalTime:Long = 0,
    val status: ChallengeStatus = ChallengeStatus.NONE,
    val appGoals: List<App> = emptyList()
) {
    data class App(
        val packageName: String = "",
        val goalTime: Long = 0
    )
}
