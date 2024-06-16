package com.hmh.hamyeonham.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.hmh.hamyeonham.core.database.model.ChallengeWithUsageEntity
import com.hmh.hamyeonham.core.database.model.DailyChallengeEntity
import com.hmh.hamyeonham.core.database.model.UsageEntity

@Dao
interface ChallengeDao {

    @Transaction
    suspend fun insertChallengeWithUsages(challengeWithUsages: ChallengeWithUsageEntity) {
        insertChallenge(challengeWithUsages.challenge)
        insertUsages(challengeWithUsages.apps)
    }

    // DailyChallenge 와 연관된 Usages 조회
    @Transaction
    @Query("SELECT * FROM daily_challenges")
    suspend fun getChallengeWithUsages(): List<ChallengeWithUsageEntity>

    @Transaction
    @Query("SELECT * FROM daily_challenges WHERE challengeDate = :date")
    suspend fun getChallengeWithUsages(date: String): ChallengeWithUsageEntity?

    // 특정 DailyChallenge와 연관된 모든 데이터 삭제
    @Transaction
    suspend fun deleteCompleteChallenge(date: String) {
        deleteUsagesForChallenge(date)
        deleteChallenge(date)
    }

    // 모든 DailyChallenge와 연관된 모든 데이터 삭제
    @Transaction
    suspend fun deleteAll() {
        deleteAllUsages()
        deleteAllChallenges()
    }

    // 모든 DailyChallenge 삭제
    /**
     * 절대 단독으로 호출하지 말 것
     */
    @Query("DELETE FROM daily_challenges")
    suspend fun deleteAllChallenges()

    // 모든 DailyChallenge와 연관된 Usages 삭제
    /**
     * 절대 단독으로 호출하지 말 것
     */
    @Query("DELETE FROM usages")
    suspend fun deleteAllUsages()


    /**
     * 절대 단독으로 호출하지 말 것
     */
    @Insert
    suspend fun insertChallenge(challenge: DailyChallengeEntity): Long

    /**
     * 절대 단독으로 호출하지 말 것
     */
    @Insert
    suspend fun insertUsages(usages: List<UsageEntity>)

    // 특정 DailyChallenge 삭제
    /**
     * 절대 단독으로 호출하지 말 것
     */
    @Query("DELETE FROM daily_challenges WHERE challengeDate = :date")
    suspend fun deleteChallenge(date: String)

    // 특정 DailyChallenge와 연관된 모든 Usages 삭제 (Cascade)
    /**
     * 절대 단독으로 호출하지 말 것
     */
    @Query("DELETE FROM usages WHERE challengeDate = :date")
    suspend fun deleteUsagesForChallenge(date: String)
}