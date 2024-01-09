package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.usagestats.model.UsageStatAndGoal
import com.hmh.hamyeonham.usagestats.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.repository.UsageStatsRepository
import javax.inject.Inject

class GetUsageStatsListUseCase @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository,
    private val usageGoalsRepository: UsageGoalsRepository,
) {

    companion object {
        private const val TOTAL = "total"
    }

    fun getUsageStatsAndGoals(
        startTime: Long,
        endTime: Long,
    ): List<UsageStatAndGoal> {
        val totalUsage = getTotalUsage(startTime, endTime)
        val usageForSelectedApps = getUsageStatsAndGoalsForSelectedApps(startTime, endTime)
        val totalUsageStatAndGoal =
            UsageStatAndGoal(TOTAL, totalUsage, usageGoalsRepository.getUsageGoalTime(TOTAL))
        return listOf(totalUsageStatAndGoal) + usageForSelectedApps
    }

    private fun getUsageStatsAndGoalsForSelectedApps(
        startTime: Long,
        endTime: Long,
    ): List<UsageStatAndGoal> {
        val appList = getSelectedPackageList()
        return usageStatsRepository.getUsageStatForPackages(startTime, endTime, appList)
            .map {
                createUsageStatAndGoal(
                    it.packageName,
                    it.totalTimeInForeground,
                    it.packageName,
                )
            }
    }

    private fun getTotalUsage(
        startTime: Long,
        endTime: Long,
    ): Long = usageStatsRepository.getUsageStats(startTime, endTime).sumOf {
        it.totalTimeInForeground
    }

    private fun getSelectedPackageList(): List<String> =
        usageGoalsRepository.getUsageGoals().filter { it.packageName != TOTAL }
            .map { it.packageName }.distinct()

    private fun createUsageStatAndGoal(
        packageName: String,
        totalTimeInForeground: Long,
        goalKey: String,
    ): UsageStatAndGoal {
        val goalTime = usageGoalsRepository.getUsageGoalTime(goalKey)
        return UsageStatAndGoal(packageName, totalTimeInForeground, goalTime)
    }
}
