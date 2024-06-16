package com.hmh.hamyeonham.lock

import javax.inject.Inject

class GetIsUnLockUseCase @Inject constructor(
    private val repository: LockRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.getIsUnLock()
    }

}