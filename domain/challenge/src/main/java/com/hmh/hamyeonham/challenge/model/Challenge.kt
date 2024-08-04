package com.hmh.hamyeonham.challenge.model

import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus

data class Challenge(
    val appGoals: List<AppGoal> = emptyList(),
    val challengeList: List<ChallengeStatus> = emptyList(),
    val goalTime: Long = 0,
    val period: Int = 0,
    val todayIndex: Int = 0,
) {
    data class AppGoal(
        val appCode: String,
        val appGoalTime: Long,
    )

    val goalTimeInHours: Int
        get() = (goalTime / 1000 / 60 / 60).toInt()
}
