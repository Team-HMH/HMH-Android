package com.hmh.hamyeonham.lock

interface LockRepository {
    fun setIsUnLock(isUnLock: Boolean)
    fun getIsUnLock(): Boolean
}