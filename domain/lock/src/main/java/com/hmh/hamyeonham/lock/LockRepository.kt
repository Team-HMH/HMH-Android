package com.hmh.hamyeonham.lock

interface LockRepository {
    suspend fun setIsUnLock(isUnLock: Boolean)
    suspend fun getIsUnLock(): Boolean
}