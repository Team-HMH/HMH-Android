package com.hmh.hamyeonham.data.challenge.datasource

import com.hmh.hamyeonham.core.database.model.ChallengeWithUsageEntity
import com.hmh.hamyeonham.core.database.model.DailyChallengeEntity

interface ChallengeLocalDatasource {
    suspend fun getChallengeWithUsage(): List<DailyChallengeEntity>
    suspend fun getChallengeWithUsage(challengeDate: String): DailyChallengeEntity?
    suspend fun insertChallengeWithUsage(challengeWithUsage: ChallengeWithUsageEntity)
    suspend fun deleteChallengeWithUsage(challengeDate: String)
    suspend fun deleteAll()
}