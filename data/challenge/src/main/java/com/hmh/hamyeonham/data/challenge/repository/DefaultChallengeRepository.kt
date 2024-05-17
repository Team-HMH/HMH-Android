package com.hmh.hamyeonham.data.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.core.network.challenge.AppCodeRequest
import com.hmh.hamyeonham.core.network.challenge.ChallengeService
import com.hmh.hamyeonham.data.challenge.mapper.toAppsRequest
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeStatus
import javax.inject.Inject

class DefaultChallengeRepository @Inject constructor(
    private val challengeService: ChallengeService,
) : ChallengeRepository {
    override suspend fun getChallengeData(): Result<ChallengeStatus> {
        return runCatching { challengeService.getChallengeData().data.toChallengeStatus() }
    }

    override suspend fun postApps(request: Apps): Result<Unit> {
        return runCatching { challengeService.postApps(request.toAppsRequest()) }
    }

    override suspend fun deleteApps(appCode: String): Result<Unit> {
        return runCatching { challengeService.deleteApps(AppCodeRequest(appCode)) }
    }
}