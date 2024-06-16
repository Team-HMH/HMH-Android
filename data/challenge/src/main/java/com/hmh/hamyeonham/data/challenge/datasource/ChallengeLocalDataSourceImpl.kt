package com.hmh.hamyeonham.data.challenge.datasource

import com.hmh.hamyeonham.core.database.dao.ChallengeDao
import com.hmh.hamyeonham.core.database.model.ChallengeWithUsageEntity
import javax.inject.Inject

class ChallengeLocalDataSourceImpl @Inject constructor(
    private val challengeDao: ChallengeDao
): ChallengeLocalDatasource{
    override suspend fun getChallengeWithUsage(): List<ChallengeWithUsageEntity> {
        return challengeDao.getChallengeWithUsages()
    }

    override suspend fun getChallengeWithUsage(challengeDate: String): ChallengeWithUsageEntity? {
        return challengeDao.getChallengeWithUsages(challengeDate)
    }

    override suspend fun insertChallengeWithUsage(challengeWithUsage: ChallengeWithUsageEntity) {
        challengeDao.insertChallengeWithUsages(challengeWithUsage)
    }

    override suspend fun deleteChallengeWithUsage(challengeDate: String) {
        challengeDao.deleteCompleteChallenge(challengeDate)
    }

    override suspend fun deleteAll() {
        challengeDao.deleteAll()
    }

}