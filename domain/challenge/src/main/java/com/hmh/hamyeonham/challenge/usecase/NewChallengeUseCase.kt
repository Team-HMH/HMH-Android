package com.hmh.hamyeonham.challenge.usecase

import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import javax.inject.Inject

class NewChallengeUseCase @Inject constructor(
    private val challengeRepository: ChallengeRepository
) {
    suspend operator fun invoke(newChallenge: NewChallenge) {
        challengeRepository.generateNewChallenge(newChallenge).onSuccess {
            //ToDO: local data base에 추가해야 하나?
        }
    }
}
