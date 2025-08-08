package com.hmh.hamyeonham.data.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import timber.log.Timber
import javax.inject.Inject

class DefaultChallengeRepository @Inject constructor(
) : ChallengeRepository {

    override suspend fun postApps(request: Apps): Result<Unit> {
        return runCatching { Timber.d("DefaultChallengeRepository : postApps 함수 앱 추가 로직 ") }
    }

    override suspend fun deleteApps(appCode: String): Result<Unit> {
        return runCatching { Timber.d("DefaultChallengeRepository : deleteApps 함수 앱 삭제 로직 ") }
    }

}