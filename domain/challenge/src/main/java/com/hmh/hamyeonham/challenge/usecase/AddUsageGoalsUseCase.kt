package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.core.domain.usagegoal.model.UsageGoal
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import javax.inject.Inject

class AddUsageGoalsUseCase @Inject constructor(
    private val usageGoalsRepository: UsageGoalsRepository,
    private val challengeRepository: ChallengeRepository
) {
    suspend operator fun invoke(apps: Apps) {
        challengeRepository.postApps(apps).onSuccess {
            usageGoalsRepository.addUsageGoalList(
                apps.apps.map {
                    UsageGoal.App(it.appCode, it.goalTime)
                }
            )
        }
    }
}
