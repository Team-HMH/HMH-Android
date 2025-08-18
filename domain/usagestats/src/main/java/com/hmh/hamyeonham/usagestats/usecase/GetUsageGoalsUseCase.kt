package com.hmh.hamyeonham.usagestats.usecase

import com.hmh.hamyeonham.core.domain.usagegoal.model.AppUsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsageGoalsUseCase @Inject constructor(
    private val usageGoalsRepository: UsageGoalsRepository,
) {
    suspend operator fun invoke(): Flow<AppUsageGoal> {
        return usageGoalsRepository.getUsageGoals()
    }
}
