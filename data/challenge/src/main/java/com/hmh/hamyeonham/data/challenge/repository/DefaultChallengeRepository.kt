package com.hmh.hamyeonham.data.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsage
import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.core.network.challenge.AppCodeRequest
import com.hmh.hamyeonham.core.network.challenge.ChallengeService
import com.hmh.hamyeonham.core.network.lock.api.LockService
import com.hmh.hamyeonham.core.network.usagegoal.DailyChallengeService
import com.hmh.hamyeonham.data.challenge.datasource.ChallengeLocalDatasource
import com.hmh.hamyeonham.data.challenge.mapper.toAppsRequest
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeResult
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeStatus
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeWithUsage
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeWithUsageEntity
import com.hmh.hamyeonham.data.challenge.mapper.toNewChallengeRequest
import com.hmh.hamyeonham.data.challenge.mapper.toRequestChallengeWithUsage
import com.hmh.hamyeonham.data.challenge.mapper.toUsage
import com.hmh.hamyeonham.usagestats.repository.UsageStatsRepository
import javax.inject.Inject

class DefaultChallengeRepository @Inject constructor(
    private val challengeService: ChallengeService,
    private val dailyChallengeService: DailyChallengeService,
    private val usageStatsRepository: UsageStatsRepository,
    private val challengeLocalDatasource: ChallengeLocalDatasource,
    private val lockService: LockService
) : ChallengeRepository {

    override suspend fun getChallengeData(): Result<ChallengeStatus> {
        return runCatching { challengeService.getChallengeData().data.toChallengeStatus() }
    }

    override suspend fun getTodayResult(): Result<Boolean> {
        return runCatching { dailyChallengeService.getUsageGoal().data.toChallengeResult() }
    }

    override suspend fun updateDailyChallengeFailed(): Result<Unit> {
        return runCatching { dailyChallengeService.updateDailyChallengeFailed().data }
    }

    override suspend fun getChallengeWithUsage(): Result<List<ChallengeWithUsage>> {
        return runCatching {
            challengeLocalDatasource.getChallengeWithUsage().map { entity ->
                val challengeDate = entity.challenge.challengeDate
                val appUsageList = usageStatsRepository.getUsageStats(challengeDate)
                ChallengeWithUsage(
                    challengeDate = challengeDate,
                    isUnlock = entity.challenge.isUnlock,
                    apps = appUsageList.map { it.toUsage() }
                )
            }
        }
    }

    override suspend fun deleteAllChallengeWithUsage(): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.deleteAll()
        }
    }

    override suspend fun getChallengeWithUsage(challengeDate: String): Result<ChallengeWithUsage> {
        return runCatching {
            challengeLocalDatasource.getChallengeWithUsage(challengeDate).toChallengeWithUsage()
        }
    }

    override suspend fun insertChallengeWithUsage(challengeWithUsage: ChallengeWithUsage): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.insertChallengeWithUsage(challengeWithUsage.toChallengeWithUsageEntity())
        }

    }

    override suspend fun deleteChallengeWithUsage(challengeDate: String): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.deleteChallengeWithUsage(challengeDate)
        }
    }

    override suspend fun uploadSavedChallenge(challengeWithUsages: List<ChallengeWithUsage>): Result<Unit> {
        return runCatching {
            dailyChallengeService.postChallengeWithUsage(challengeWithUsages.toRequestChallengeWithUsage())
        }
    }

    override suspend fun getDailyChallengeIsUnlock(date: String): Boolean {
        val challenge = challengeLocalDatasource.getChallengeWithUsage(date).toChallengeWithUsage()
        return runCatching {
            challenge.isUnlock ?: (lockService.getIsLockedWithDate(date).data.isLockToday ?: false)
        }.getOrDefault(false)
    }

    override suspend fun setDailyChallengeIsUnlock(
        date: String,
        isUnLock: Boolean
    ) {
        val challenge = challengeLocalDatasource.getChallengeWithUsage(date).toChallengeWithUsage()
        setDailyChallengeIsUnlock(challenge, isUnLock)
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

    private suspend fun setDailyChallengeIsUnlock(
        challenge: ChallengeWithUsage,
        isUnlock: Boolean
    ) {
        challengeLocalDatasource.insertChallengeWithUsage(
            ChallengeWithUsage(
                challengeDate = challenge.challengeDate,
                isUnlock = isUnlock,
                apps = challenge.apps
            ).toChallengeWithUsageEntity()
        )
    }

}