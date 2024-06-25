package com.hmh.hamyeonham.lock

import javax.inject.Inject

class UpdateIsUnLockUseCase @Inject constructor(
    private val repository: LockRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.updateIsUnLock()
    }
}