package com.hmh.hamyeonham.lock

import javax.inject.Inject

class SetIsUnLockUseCase @Inject constructor(
    private val repository: LockRepository
) {
    suspend operator fun invoke(isUnLock: Boolean): Result<Unit> {
        return repository.setIsUnLock(isUnLock)
    }
}