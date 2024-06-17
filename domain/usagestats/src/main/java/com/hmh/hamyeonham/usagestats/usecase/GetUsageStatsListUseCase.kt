package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.model.UsageStatus
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import com.hmh.hamyeonham.usagestats.model.sumUsageStats
import com.hmh.hamyeonham.usagestats.repository.DeleteGoalRepository
import com.hmh.hamyeonham.usagestats.repository.UsageStatsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetUsageStatsListUseCase @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository,
    private val usageGoalsRepository: UsageGoalsRepository,
    private val deleteGoalRepository: DeleteGoalRepository
) {

    companion object {
        private const val TOTAL = "total"
    }

    suspend operator fun invoke(
        startTime: Long,
        endTime: Long,
    ): List<UsageStatusAndGoal> {
        usageGoalsRepository.getUsageGoals().first().let { usageGoals ->
            val selectedPackages = getSelectedPackageList(usageGoals)
            val usageForSelectedApps = getUsageStatsAndGoalsForSelectedPackages(
                startTime,
                endTime,
                selectedPackages,
            )
            val usageStatusAndGoalsForSelectedApps = usageForSelectedApps.map {
                UsageStatusAndGoal(
                    it.packageName,
                    it.totalTimeInForeground,
                    getUsageGoalForPackage(usageGoals, it.packageName),
                )
            }
            val totalUsage = usageForSelectedApps.sumUsageStats() + deleteGoalRepository.getDeletedUsageOfToday()
            val totalUsageStatusAndGoal = UsageStatusAndGoal(
                TOTAL,
                totalUsage,
                getUsageGoalForPackage(usageGoals, TOTAL),
            )
            return listOf(totalUsageStatusAndGoal) + usageStatusAndGoalsForSelectedApps.sortedByDescending { it.usedPercentage }
        }
    }

    private fun getUsageGoalForPackage(
        usageGoalsForSelectedPackages: List<UsageGoal>,
        packageName: String,
    ): Long {
        usageGoalsForSelectedPackages.forEach {
            if (it.packageName == packageName) {
                return it.goalTime
            }
        }
        return 0
    }

    private suspend fun getUsageStatsAndGoalsForSelectedPackages(
        startTime: Long,
        endTime: Long,
        selectedPackages: List<String>,
    ): List<UsageStatus> {
        return usageStatsRepository.getUsageStatForPackages(startTime, endTime, selectedPackages)
    }

    private fun getSelectedPackageList(usageGoalList: List<UsageGoal>): List<String> =
        usageGoalList.filter { it.packageName != TOTAL }
            .map { it.packageName }.distinct()
}
