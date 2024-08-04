package com.hmh.hamyeonham.data.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.Challenge
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsageInput
import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.core.network.challenge.AppCodeRequest
import com.hmh.hamyeonham.core.network.challenge.ChallengeService
import com.hmh.hamyeonham.core.network.usagegoal.DailyChallengeService
import com.hmh.hamyeonham.data.challenge.datasource.ChallengeLocalDatasource
import com.hmh.hamyeonham.data.challenge.mapper.toAppsRequest
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

    override suspend fun getChallengeData(): Result<Challenge> {
        return runCatching { challengeService.getChallengeData().data.toChallengeStatus() }
    }

    override suspend fun updateDailyChallengeFailed(): Result<Unit> {
        return runCatching { dailyChallengeService.updateDailyChallengeFailed().data }
    }

    override suspend fun getChallengeWithUsage(): Result<List<ChallengeWithUsageInput>> {
        return runCatching {
            val (startTime, endTime) = getCurrentDayStartEndEpochMillis()
            challengeLocalDatasource.getChallengeWithUsage().map { entity ->
                val challengeDate = entity.challengeDate
                val appUsageList = getUsageStatsListUseCase(
                    startTime = startTime,
                    endTime = endTime,
                )
                ChallengeWithUsageInput(
                    challengeDate = challengeDate,
                    apps = appUsageList.apps.map {
                        ChallengeWithUsageInput.Usage(
                            packageName = it.packageName,
                            usageTime = it.usageTime
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

    override suspend fun insertChallengeWithUsage(challengeWithUsageInput: ChallengeWithUsageInput): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.insertChallengeWithUsage(challengeWithUsageInput.toChallengeWithUsageEntity())
        }

    }

    override suspend fun deleteChallengeWithUsage(challengeDate: String): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.deleteChallengeWithUsage(challengeDate)
        }
    }

    override suspend fun uploadSavedChallenge(challengeWithUsageInputs: List<ChallengeWithUsageInput>): Result<Unit> {
        return runCatching {
            dailyChallengeService.postChallengeWithUsage(challengeWithUsageInputs.toRequestChallengeWithUsage())
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