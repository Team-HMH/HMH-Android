package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import javax.inject.Inject

class NewChallengeUseCase @Inject constructor(
    private val challengeRepository: ChallengeRepository
) {
    suspend operator fun invoke(newChallenge: NewChallenge): Result<Unit> {
        return challengeRepository.generateNewChallenge(newChallenge)
    }
}
