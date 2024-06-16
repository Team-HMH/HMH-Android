package com.hmh.hamyeonham.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsage
import com.hmh.hamyeonham.challenge.model.NewChallenge

interface ChallengeRepository {
    suspend fun getChallengeData(): Result<ChallengeStatus>
    suspend fun getTodayResult(): Result<Boolean>
    suspend fun postApps(request: Apps): Result<Unit>
    suspend fun deleteApps(appCode: String): Result<Unit>
    suspend fun generateNewChallenge(request: NewChallenge): Result<Unit>
    suspend fun updateDailyChallengeFailed(): Result<Unit>
    suspend fun getChallengeWithUsage(): Result<List<ChallengeWithUsage>>
    suspend fun getChallengeWithUsage(challengeDate: String): Result<ChallengeWithUsage>
    suspend fun insertChallengeWithUsage(challengeWithUsage: ChallengeWithUsage): Result<Unit>
    suspend fun deleteChallengeWithUsage(challengeDate: String): Result<Unit>
    suspend fun deleteAllChallengeWithUsage(): Result<Unit>
    suspend fun uploadSavedChallenge(challengeWithUsages: List<ChallengeWithUsage>): Result<Unit>
}
