package com.hmh.hamyeonham.lock

import javax.inject.Inject

class SetIsUnLockUseCase @Inject constructor(
    private val repository: LockRepository
){
    operator fun invoke(isUnLock: Boolean){
        repository.setIsUnLock(isUnLock)
    }
}