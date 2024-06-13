package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.usagestats.repository.DeleteGoalRepository
import javax.inject.Inject

class DeletedAppUsageStoreUseCase @Inject constructor(
    private val deleteGoalRepository: DeleteGoalRepository
) {
    suspend operator fun invoke(totalUsage: Long, packageName: String) {
        deleteGoalRepository.addDeletedGoal(packageName, totalUsage)
    }
}