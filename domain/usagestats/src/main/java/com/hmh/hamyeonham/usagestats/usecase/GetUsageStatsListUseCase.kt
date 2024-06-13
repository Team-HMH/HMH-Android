package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.model.UsageStatusAndGoal
import com.hmh.hamyeonham.usagestats.repository.UsageStatsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetUsageStatsListUseCase @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository,
    private val usageGoalsRepository: UsageGoalsRepository,
    private val getTotalUsageStatsUseCase: GetTotalUsageStatsUseCase
) {

    companion object {
        private const val TOTAL = "total"
    }

    suspend operator fun invoke(
        startTime: Long,
        endTime: Long,
    ): List<UsageStatusAndGoal> {
        usageGoalsRepository.getUsageGoals().first().let { usageGoals ->
            val usageForSelectedApps = getUsageStatsAndGoalsForSelectedPackages(
                startTime,
                endTime,
                usageGoals.filter { it.packageName != TOTAL },
            )
            val totalUsage = getTotalUsageStatsUseCase(usageForSelectedApps)
            val totalUsageStatusAndGoal = UsageStatusAndGoal(
                TOTAL,
                totalUsage,
                getUsageGoalForPackage(usageGoals, TOTAL),
            )
            return listOf(totalUsageStatusAndGoal) + usageForSelectedApps.sortedByDescending { it.usedPercentage }
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
        usageGoalList: List<UsageGoal>,
    ): List<UsageStatusAndGoal> {
        val selectedPackage = getSelectedPackageList(usageGoalList)
        return usageStatsRepository.getUsageStatForPackages(startTime, endTime, selectedPackage)
            .map {
                createUsageStatAndGoal(
                    it.packageName,
                    it.totalTimeInForeground,
                    getUsageGoalForPackage(usageGoalList, it.packageName),
                )
            }
    }

    private fun getSelectedPackageList(usageGoalList: List<UsageGoal>): List<String> =
        usageGoalList.filter { it.packageName != TOTAL }
            .map { it.packageName }.distinct()

    private fun createUsageStatAndGoal(
        packageName: String,
        totalTimeInForeground: Long,
        goalTime: Long,
    ): UsageStatusAndGoal {
        return UsageStatusAndGoal(packageName, totalTimeInForeground, goalTime)
    }
}
