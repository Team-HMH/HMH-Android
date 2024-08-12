package com.hmh.hamyeonham.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.Challenge
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsageInput
import com.hmh.hamyeonham.challenge.model.NewChallenge

interface ChallengeRepository {
    data class ChallengeDateWithIndex(
        val date: String,
        val index: Int
    )
    suspend fun getChallengeData(): Result<Challenge>
    suspend fun postApps(request: Apps): Result<Unit>
    suspend fun deleteApps(appCode: String): Result<Unit>
    suspend fun generateNewChallenge(request: NewChallenge): Result<Unit>
    suspend fun updateDailyChallengeFailed(): Result<Unit>
    suspend fun insertChallengeWithUsage(challengeWithUsageInput: ChallengeWithUsageInput): Result<Unit>
    suspend fun deleteChallengeWithUsage(challengeDate: String): Result<Unit>
    suspend fun deleteAllChallengeWithUsage(): Result<Unit>
}
