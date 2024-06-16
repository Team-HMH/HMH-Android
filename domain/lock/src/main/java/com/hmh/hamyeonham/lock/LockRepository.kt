package com.hmh.hamyeonham.lock

interface LockRepository {
    suspend fun setIsUnLock(isUnLock: Boolean): Result<Unit>
    suspend fun getIsUnLock(): Boolean
}