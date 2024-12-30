package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.model.UsageStatus
import com.hmh.hamyeonham.usagestats.model.sumUsageStats
import com.hmh.hamyeonham.usagestats.repository.DeleteGoalRepository
import com.hmh.hamyeonham.usagestats.repository.UsageStatsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTotalUsageStatsUseCase @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository,
    private val usageGoalsRepository: UsageGoalsRepository,
    private val deleteGoalRepository: DeleteGoalRepository
) {

    suspend operator fun invoke(
        startTime: Long,
        endTime: Long,
    ): Long {
        usageGoalsRepository.getUsageGoals().first().let { usageGoals ->
            val usageStatsForSelectedPackages = getUsageStatsForSelectedPackages(
                startTime,
                endTime,
                getPackageNamesFromUsageGoals(usageGoals)
            )
            return calculateTotalUsage(usageStatsForSelectedPackages)
        }
    }

    private suspend fun calculateTotalUsage(usageStats: List<UsageStatus>): Long {
        return usageStats.sumUsageStats() + deleteGoalRepository.getDeletedUsageOfToday()
    }

    private suspend fun getUsageStatsForSelectedPackages(
        startTime: Long,
        endTime: Long,
        packageNames: List<String>,
    ): List<UsageStatus> {
        return usageStatsRepository.getUsageStatForPackages(startTime, endTime, packageNames)
    }

    private fun getPackageNamesFromUsageGoals(usageGoals: UsageGoal): List<String> {
        return usageGoals.appGoals.map { it.packageName }
    }
}
