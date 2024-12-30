package com.hmh.hamyeonham.data.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps
import com.hmh.hamyeonham.challenge.model.Challenge
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsageInput
import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.currentDate
import com.hmh.hamyeonham.common.time.getDayStartEndEpochMillis
import com.hmh.hamyeonham.core.domain.usagegoal.model.ChallengeStatus
import com.hmh.hamyeonham.core.network.challenge.AppCodeRequest
import com.hmh.hamyeonham.core.network.challenge.ChallengeService
import com.hmh.hamyeonham.core.network.usagegoal.DailyChallengeService
import com.hmh.hamyeonham.data.challenge.datasource.ChallengeLocalDatasource
import com.hmh.hamyeonham.data.challenge.mapper.toAppsRequest
import com.hmh.hamyeonham.data.challenge.mapper.toChallengeStatus
import com.hmh.hamyeonham.data.challenge.mapper.toNewChallengeRequest
import com.hmh.hamyeonham.data.challenge.mapper.toRequestChallengeWithUsage
import com.hmh.hamyeonham.usagestats.usecase.GetUsageStatsListUseCase
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import javax.inject.Inject

class DefaultChallengeRepository @Inject constructor(
    private val challengeService: ChallengeService,
    private val dailyChallengeService: DailyChallengeService,
    private val getUsageStatsListUseCase: GetUsageStatsListUseCase,
    private val challengeLocalDatasource: ChallengeLocalDatasource,
) : ChallengeRepository {

    override suspend fun getChallengeData(): Result<Challenge> {
        return runCatching {
            val challenge = challengeService.getChallengeData().data.toChallengeStatus()

            val challengeWithIndex = challenge.challengeList.mapIndexedNotNull { index, challengeStatus ->
                if (challengeStatus != ChallengeStatus.NONE || challenge.todayIndex <= index) {
                    null
                } else {
                    val dateIndexDifference = index - challenge.todayIndex
                    val challengeDate = currentDate.plus(dateIndexDifference, DateTimeUnit.DAY)
                    ChallengeRepository.ChallengeDateWithIndex(
                        date = challengeDate.toString(),
                        index = index
                    )
                }
            }

            if (challengeWithIndex.isNotEmpty()) {
                val challengeStatus = uploadSavedChallenge(challengeWithIndex).getOrThrow()
                challenge.copy(challengeList = challengeStatus ?: challenge.challengeList)
            } else {
                challenge
            }
        }
    }

    override suspend fun updateDailyChallengeFailed(): Result<Unit> {
        return runCatching { dailyChallengeService.updateDailyChallengeFailed().data }
    }

    override suspend fun deleteAllChallengeWithUsage(): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.deleteAll()
        }
    }

    override suspend fun insertChallengeWithUsage(challengeWithUsageInput: ChallengeWithUsageInput): Result<Unit> {
        return runCatching {}
    }

    override suspend fun deleteChallengeWithUsage(challengeDate: String): Result<Unit> {
        return runCatching {
            challengeLocalDatasource.deleteChallengeWithUsage(challengeDate)
        }
    }

    private suspend fun uploadSavedChallenge(challengeDateWithIndex: List<ChallengeRepository.ChallengeDateWithIndex>): Result<List<ChallengeStatus>?> {
        val challengeWithUsages = getChallengeWithUsage(challengeDateWithIndex).getOrThrow()
        return runCatching {
            val request = challengeWithUsages.toRequestChallengeWithUsage()
            val response = dailyChallengeService.postChallengeWithUsage(request).data
            response.statuses?.map { ChallengeStatus.fromString(it) }
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

    private suspend fun getChallengeWithUsage(
        challengeDateWithIndex: List<ChallengeRepository.ChallengeDateWithIndex>
    ): Result<List<ChallengeWithUsageInput>> {
        return runCatching {
            challengeDateWithIndex.map { (challengeDate, index) ->
                val (startTime, endTime) = getDayStartEndEpochMillis(LocalDate.parse(challengeDate))
                val appUsageList = getUsageStatsListUseCase(
                    startTime = startTime,
                    endTime = endTime,
                )
                ChallengeWithUsageInput(
                    challengePeriodIndex = index,
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
}