package com.hmh.hamyeonham.data.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.core.network.challenge.AppCodeRequest
import com.hmh.hamyeonham.core.network.challenge.ChallengeService
import com.hmh.hamyeonham.core.network.usagegoal.DailyChallengeService
import com.hmh.hamyeonham.data.challenge.mapper.toAppsRequest
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeStatus
import com.hmh.hamyeonham.data.challenge.mapper.toNewChallengeRequest
import javax.inject.Inject

class DefaultChallengeRepository @Inject constructor(
    private val challengeService: ChallengeService,
    private val dailyChallengeService: DailyChallengeService
) :
    ChallengeRepository {
    override suspend fun getChallengeData(): Result<ChallengeStatus> {
        return runCatching { challengeService.getChallengeData().data.toChallengeStatus() }
    }

    override suspend fun updateDailyChallengeFailed(): Result<Unit> {
        return runCatching { dailyChallengeService.updateDailyChallengeFailed().data }
    }

    override suspend fun postApps(request: Apps): Result<Unit> {
        return runCatching { challengeService.postApps(request.toAppsRequest()) }
    }

    override suspend fun deleteApps(appCode: String): Result<Unit> {
        return runCatching { challengeService.deleteApps(AppCodeRequest(appCode)) }
    }

    override suspend fun generateNewChallenge(request: NewChallenge): Result<Unit> {
        return runCatching { challengeService.postNewChallenge(request.toNewChallengeRequest()) }
    }

}