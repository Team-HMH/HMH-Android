package com.hmh.hamyeonham.data.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.data.challenge.datasource.ChallengeLocalDatasource
import timber.log.Timber
import javax.inject.Inject

class DefaultChallengeRepository @Inject constructor(
    private val challengeLocalDatasource: ChallengeLocalDatasource,
) : ChallengeRepository {

    override suspend fun updateDailyChallengeFailed(): Result<Unit> {
        return runCatching {
            Timber.d("Update daily challenge failed")
        }
    }

    override suspend fun deleteAllChallengeWithUsage(): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.deleteAll()
        }
    }

    override suspend fun deleteChallengeWithUsage(challengeDate: String): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.deleteChallengeWithUsage(challengeDate)
        }
    }

    override suspend fun postApps(request: Apps): Result<Unit> {
        return runCatching { Timber.d("Post apps: $request") }
    }

    override suspend fun deleteApps(appCode: String): Result<Unit> {
        return runCatching { Timber.d("Delete app with code: $appCode") }
    }
}