package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import javax.inject.Inject

class DeleteAllDatabaseUseCase @Inject constructor(
    private val usageGoalsRepository: UsageGoalsRepository,
){
    suspend fun invoke(){
        usageGoalsRepository.deleteAllUsageGoal()
    }
}