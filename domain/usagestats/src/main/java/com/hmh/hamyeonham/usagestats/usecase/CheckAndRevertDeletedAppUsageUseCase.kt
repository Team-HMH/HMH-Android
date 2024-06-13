package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.usagestats.datastore.HMHDeletedAppUsagePreference
import com.hmh.hamyeonham.usagestats.repository.UsageStatsRepository
import javax.inject.Inject

class CheckAndRevertDeletedAppUsageUseCase @Inject constructor(
    private val hmhDeletedAppUsagePreference: HMHDeletedAppUsagePreference,
    private val usageStatsRepository: UsageStatsRepository
) {
    suspend operator fun invoke(packageNames: List<String>) {
        val deletedPackageNames = hmhDeletedAppUsagePreference.deletedPackageName

        for (packageName in packageNames) {
            if (deletedPackageNames.contains(packageName)) {
                val usage = usageStatsRepository.getUsageStatForPackageOfToday(packageName)
                hmhDeletedAppUsagePreference.totalUsage =
                    hmhDeletedAppUsagePreference.totalUsage - usage
                hmhDeletedAppUsagePreference.deletedPackageName =
                    hmhDeletedAppUsagePreference.deletedPackageName - packageName
            }
        }
    }
}