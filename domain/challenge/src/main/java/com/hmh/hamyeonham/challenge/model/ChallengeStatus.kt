package com.hmh.hamyeonham.challenge.model


data class ChallengeStatus(
    val apps: List<App> = emptyList(),
    val isSuccessList: List<Boolean?> = emptyList(),
    val goalTime: Int = 0,
    val period: Int = 0,
) {
    data class App(
        val appCode: String,
        val appGoalTime: Int
    )
}
