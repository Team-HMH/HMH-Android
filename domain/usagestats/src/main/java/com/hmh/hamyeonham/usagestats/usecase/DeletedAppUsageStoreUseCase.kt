package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.usagestats.datastore.HMHDeletedAppUsagePreference
import javax.inject.Inject

class DeletedAppUsageStoreUseCase @Inject constructor(
    private val hmhDeletedAppUsagePreference: HMHDeletedAppUsagePreference,
) {
    operator fun invoke(totalUsage: Long, packageName: String) {
        hmhDeletedAppUsagePreference.totalUsage =
            hmhDeletedAppUsagePreference.totalUsage + totalUsage
        hmhDeletedAppUsagePreference.deletedPackageName =
            hmhDeletedAppUsagePreference.deletedPackageName + packageName
    }
}