package com.hmh.hamyeonham.data.lock

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
    private val date: String get() = getNowDateNumeric()

    override suspend fun setIsUnLock(isUnLock: Boolean): Result<Unit> {
        val entity = LockWithDateEntity(
            date = date,
            isUnLock = isUnLock
        )
        return runCatching {
            runCatching {
                lockService.postLockWithDate(RequestLockDate(date))
            }.onSuccess {
                lockDao.insertLockWithDateEntity(entity)
            }.onFailure {
                throw it
            }
        }
    }

    override suspend fun getIsUnLock(): Boolean {
        return lockDao.getLockWithDate(date)?.isUnLock ?: false
    }

    override suspend fun updateIsUnLock(): Result<Unit> {
        return runCatching {
            runCatching {
                lockService.getIsLockedWithDate(date)
            }.onSuccess {
                lockDao.insertLockWithDateEntity(
                    LockWithDateEntity(
                        date = date,
                        isUnLock = it.data.isLockToday ?: false
                    )
                )
            }.onFailure {
                lockDao.insertLockWithDateEntity(
                    LockWithDateEntity(
                        date = date,
                        isUnLock = false
                    )
                )
            }
        }
    }
}