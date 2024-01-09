package com.hmh.hamyeonham.statistics

import androidx.lifecycle.ViewModel
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.usagestats.model.UsageStatAndGoal
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class StaticsViewModel @Inject constructor(
    private val getUsageStatsListUseCase: GetUsageStatsListUseCase,
) : ViewModel() {
    val usageStatAndGoalList = MutableStateFlow<List<UsageStatAndGoal>>(emptyList())

    init {
        val (startTime, endTime) = getCurrentDayStartEndEpochMillis()
        getUsageStatsAndGoals(startTime, endTime)
    }

    private fun getUsageStatsAndGoals(startTime: Long, endTime: Long) {
        usageStatAndGoalList.value =
            getUsageStatsListUseCase.getUsageStatsAndGoals(startTime, endTime)
    }
}
