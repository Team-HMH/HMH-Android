package com.hmh.hamyeonham.data.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsage
import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.core.network.challenge.AppCodeRequest
import com.hmh.hamyeonham.core.network.challenge.ChallengeService
import com.hmh.hamyeonham.core.network.usagegoal.DailyChallengeService
import com.hmh.hamyeonham.data.challenge.datasource.ChallengeLocalDatasource
import com.hmh.hamyeonham.data.challenge.mapper.toAppsRequest
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeResult
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeStatus
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeWithUsageEntity
import com.hmh.hamyeonham.data.challenge.mapper.toNewChallengeRequest
import com.hmh.hamyeonham.data.challenge.mapper.toRequestChallengeWithUsage
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatsListUseCase
import javax.inject.Inject

class DefaultChallengeRepository @Inject constructor(
    private val challengeService: ChallengeService,
    private val dailyChallengeService: DailyChallengeService,
    private val getUsageStatsListUseCase: GetUsageStatsListUseCase,
    private val challengeLocalDatasource: ChallengeLocalDatasource,
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
            val (startTime, endTime) = getCurrentDayStartEndEpochMillis()
            challengeLocalDatasource.getChallengeWithUsage().map { entity ->
                val challengeDate = entity.challengeDate
                val appUsageList = getUsageStatsListUseCase(
                    startTime = startTime,
                    endTime = endTime,
                )
                ChallengeWithUsage(
                    challengeDate = challengeDate,
                    apps = appUsageList.filter { it.packageName != "total" }.map {
                        ChallengeWithUsage.Usage(
                            packageName = it.packageName,
                            usageTime = it.totalTimeInForeground
                        )
                    },
                )
            }
        }
    }

    override suspend fun deleteAllChallengeWithUsage(): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.deleteAll()
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