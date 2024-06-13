package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.usagestats.repository.DeleteGoalRepository
import javax.inject.Inject

class CheckAndRevertDeletedAppUsageUseCase @Inject constructor(
    private val deleteGoalRepository: DeleteGoalRepository
) {
    suspend operator fun invoke(packageNames: List<String>) {
        for (packageName in packageNames)
            deleteGoalRepository.checkAndRevertDeletedGoal(packageName)
    }
}