package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import javax.inject.Inject

class DeleteUsageGoalUseCase @Inject constructor(
    private val usageGoalsRepository: UsageGoalsRepository,
    private val challengeRepository: ChallengeRepository
) {
    suspend operator fun invoke(appCode: String) {
        challengeRepository.deleteApps(appCode).onSuccess {
            usageGoalsRepository.deleteUsageGoal(appCode)
        }
    }
}
