package com.hmh.hamyeonham.usagestats.datasource

import com.hmh.hamyeonham.usagestats.model.UsageGoalModel
import javax.inject.Inject

class UsageGoalsDataSource @Inject constructor() {
    var usageGoalList =
        listOf(
            UsageGoalModel("total", 20000000),
            UsageGoalModel("com.kakao.talk", 3000000),
            UsageGoalModel("com.netflix.mediaclient", 3000000),
            UsageGoalModel("com.hmh.hamyeonham", 3000000),
            UsageGoalModel("com.google.android.gms", 500000),
            UsageGoalModel("com.google.android.youtube", 3000000),
            UsageGoalModel("com.android.chrome", 1000000),
            UsageGoalModel("com.apple.android.music", 1000000),
            UsageGoalModel("com.duolingo", 1000000),
            UsageGoalModel("com.android.providers.calendar", 1000000),
            UsageGoalModel("com.lge.sizechangable.weather.platform", 1000000),
            UsageGoalModel("com.discord", 1000000),
        )

    fun getUsageGoals(): List<UsageGoalModel> {
        return usageGoalList
    }
}
