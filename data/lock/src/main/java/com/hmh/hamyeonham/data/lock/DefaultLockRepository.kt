package com.hmh.hamyeonham.data.lock

import com.hmh.hamyeonham.core.network.auth.datastore.network.HMHNetworkPreference
import com.hmh.hamyeonham.lock.LockRepository
import javax.inject.Inject

class DefaultLockRepository @Inject constructor(
    private val preference: HMHNetworkPreference
) : LockRepository {
    override fun setIsUnLock(isUnLock: Boolean) {
        preference.isUnlock = isUnLock
    }

    override fun getIsUnLock(): Boolean {
        return preference.isUnlock
    }

}