package com.hmh.hamyeonham.data.lock

import android.util.Log
import com.hmh.hamyeonham.common.time.getNowDateNumeric
import com.hmh.hamyeonham.core.database.dao.LockDao
import com.hmh.hamyeonham.core.database.model.LockWithDateEntity
import com.hmh.hamyeonham.core.network.lock.api.LockService
import com.hmh.hamyeonham.core.network.lock.model.RequestLockDate
import com.hmh.hamyeonham.lock.LockRepository
import javax.inject.Inject

class DefaultLockRepository @Inject constructor(
    private val lockService: LockService,
    private val lockDao: LockDao,
) : LockRepository {
    @Volatile
    private var isUnLockFetching = false
    private val date: String get() = getNowDateNumeric()

    override suspend fun setIsUnLock(isUnLock: Boolean): Result<Unit> {
        val entity = LockWithDateEntity(
            date = date,
            isUnLock = isUnLock
        )
        return runCatching {
            lockService.postLockWithDate(RequestLockDate(date))
            lockDao.insertLockWithDateEntity(entity)
        }
    }

    override suspend fun getIsUnLock(): Boolean {
        return getDailyChallengeIsUnlock(date)
    }

    private suspend fun getDailyChallengeIsUnlock(date: String): Boolean {
        if (isUnLockFetching) return false
        isUnLockFetching = true
        val isUnLock = lockDao.getLockWithDate(date)?.isUnLock
        return runCatching {
            isUnLock ?: run {
                runCatching {
                    (lockService.getIsLockedWithDate(date).data.isLockToday ?: false)
                }.onSuccess { isUnLock ->
                    val lockWithDateEntity = LockWithDateEntity(
                        date = date,
                        isUnLock = isUnLock
                    )
                    lockDao.insertLockWithDateEntity(lockWithDateEntity)
                    lockDao.deleteLockWithoutDate(date)
                }.onFailure {
                    Log.e("LockRepository", "getDailyChallengeIsUnlock: $it")
                }.getOrDefault(false).also {
                    isUnLockFetching = false
                }
            }
        }.getOrDefault(false)
    }
}