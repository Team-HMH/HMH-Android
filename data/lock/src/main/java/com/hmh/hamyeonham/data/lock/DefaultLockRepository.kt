package com.hmh.hamyeonham.data.lock

import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.getNowDateNumeric
import com.hmh.hamyeonham.lock.LockRepository
import javax.inject.Inject

class DefaultLockRepository @Inject constructor(
    private val challengeRepository: ChallengeRepository,
) : LockRepository {
    private val date: String get() = getNowDateNumeric()
    override suspend fun setIsUnLock(isUnLock: Boolean) {
        challengeRepository.setDailyChallengeIsUnlock(
            date = date,
            isUnLock = isUnLock
        )
    }

    override suspend fun getIsUnLock(): Boolean {
        return challengeRepository.getDailyChallengeIsUnlock(date)
    }

}