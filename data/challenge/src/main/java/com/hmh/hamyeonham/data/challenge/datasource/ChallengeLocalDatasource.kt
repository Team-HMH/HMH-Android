package com.hmh.hamyeonham.data.challenge.datasource

import com.hmh.hamyeonham.core.database.model.ChallengeWithUsageEntity

interface ChallengeLocalDatasource {
    suspend fun getChallengeWithUsage(): List<ChallengeWithUsageEntity>
    suspend fun getChallengeWithUsage(challengeDate: String): ChallengeWithUsageEntity
    suspend fun insertChallengeWithUsage(challengeWithUsage: ChallengeWithUsageEntity)
    suspend fun deleteChallengeWithUsage(challengeDate: String)
    suspend fun deleteAll()
}