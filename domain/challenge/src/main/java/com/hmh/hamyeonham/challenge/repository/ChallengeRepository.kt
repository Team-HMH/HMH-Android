package com.hmh.hamyeonham.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.ChallengeStatus

interface ChallengeRepository {
    suspend fun getChallengeData(): Result<ChallengeStatus>
    suspend fun postApps(request: Apps): Result<Unit>
    suspend fun deleteApps(appCode: String): Result<Unit>
    suspend fun updateDailyChallengeFailed(): Result<Unit>
}
