package com.hmh.hamyeonham.core.viewmodel

import com.hmh.hamyeonham.challenge.model.Challenge
import com.hmh.hamyeonham.common.time.getCurrentDateOfDefaultTimeZone
import com.hmh.hamyeonham.common.time.minusDaysFromDate
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import kotlinx.datetime.LocalDate

data class MainState(
    val appGoals: List<Challenge.AppGoal> = emptyList(),
    val totalGoalTimeInHour: Int = 0,
    val period: Int = 0,
    val todayIndex: Int = 0,
    val usageGoals: UsageGoal = UsageGoal(),
    val name: String = "",
    val challengeSuccess: Boolean = true,
    var permissionGranted: Boolean = true,
) {
    val startDate: LocalDate = minusDaysFromDate(getCurrentDateOfDefaultTimeZone(), todayIndex)
    val isChallengeExist: Boolean = todayIndex != -1
    val todayIndexAsDate: Int = todayIndex + 1
}