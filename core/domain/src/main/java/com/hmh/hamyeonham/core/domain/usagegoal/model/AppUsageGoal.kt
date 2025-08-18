package com.hmh.hamyeonham.core.domain.usagegoal.model

data class AppUsageGoal(
    val appGoals: List<App> = emptyList()
) {
    data class App(
        val packageName: String = "",
        val goalTime: Long = 0
    )
}
