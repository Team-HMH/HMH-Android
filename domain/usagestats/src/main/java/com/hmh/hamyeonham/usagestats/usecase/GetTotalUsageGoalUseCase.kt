package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.core.domain.usagegoal.model.AppUsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTotalUsageGoalUseCase @Inject constructor(
    private val usageGoalsRepository: UsageGoalsRepository,
) {
    suspend operator fun invoke(): AppUsageGoal {
        return usageGoalsRepository.getUsageGoals().first()
    }
}
