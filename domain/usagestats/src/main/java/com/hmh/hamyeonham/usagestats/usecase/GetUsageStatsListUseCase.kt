package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.core.domain.usagegoal.model.AppUsageGoal
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

    suspend operator fun invoke(
        startTime: Long,
        endTime: Long,
    ): UsageStatusAndGoal {
        usageGoalsRepository.getUsageGoals().first().let { usageGoal ->
            val selectedPackages = getSelectedPackageList(usageGoal)
            val usageForSelectedApps = getUsageStatsAndGoalsForSelectedPackages(
                startTime,
                endTime,
                selectedPackages,
            )
            val totalUsage = usageForSelectedApps.sumUsageStats() + deleteGoalRepository.getDeletedUsageOfToday()

            val usageStatusAndGoal = UsageStatusAndGoal(
                totalTimeInForeground = totalUsage,
                apps = usageGoal.appGoals.map {
                    UsageStatusAndGoal.App(
                        packageName = it.packageName,
                        usageTime = usageForSelectedApps.find { usageStatus -> usageStatus.packageName == it.packageName }?.totalTimeInForeground ?: 0,
                        goalTime = it.goalTime,
                    )
                }.sortedByDescending { it.usageTime },
            )

            return usageStatusAndGoal
        }
    }

    private suspend fun getUsageStatsAndGoalsForSelectedPackages(
        startTime: Long,
        endTime: Long,
        selectedPackages: List<String>,
    ): List<UsageStatus> {
        return usageStatsRepository.getUsageStatForPackages(startTime, endTime, selectedPackages)
    }

    private fun getSelectedPackageList(appUsageGoalList: AppUsageGoal): List<String> =
        appUsageGoalList.appGoals.map { it.packageName }.distinct()
}
