package com.hmh.hamyeonham.data.challenge.datasource

import com.hmh.hamyeonham.core.database.dao.ChallengeDao
import com.hmh.hamyeonham.core.database.model.ChallengeWithUsageEntity
import com.hmh.hamyeonham.core.database.model.DailyChallengeEntity
import javax.inject.Inject

class ChallengeLocalDataSourceImpl @Inject constructor(
    private val challengeDao: ChallengeDao
): ChallengeLocalDatasource{
    override suspend fun getChallengeWithUsage(): List<DailyChallengeEntity> {
        return challengeDao.getDailyChallenge()
    }

    override suspend fun getChallengeWithUsage(challengeDate: String): DailyChallengeEntity? {
        return challengeDao.getDailyChallenge(challengeDate)
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