package com.hmh.hamyeonham.data.challenge.mapper

import com.hmh.hamyeonham.challenge.model.Challenge
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsageInput
import com.hmh.hamyeonham.core.database.model.ChallengeWithUsageEntity
import com.hmh.hamyeonham.core.database.model.DailyChallengeEntity
import com.hmh.hamyeonham.core.database.model.UsageEntity
import com.hmh.hamyeonham.core.network.challenge.model.ChallengeResponse
import com.hmh.hamyeonham.core.network.challenge.model.ChallengeWithUsageRequest

internal fun ChallengeResponse.toChallengeStatus(): Challenge {
    return Challenge(
        apps.map {
            Challenge.AppGoal(it.appCode, it.goalTime)
        },
        statuses.toStatusList(todayIndex),
        goalTime,
        period,
        todayIndex,
    )
}

internal fun ChallengeWithUsageInput.toChallengeWithUsageEntity(): ChallengeWithUsageEntity {
    return ChallengeWithUsageEntity(
        challenge = DailyChallengeEntity(challengeDate = challengeDate),
        apps = apps.map { UsageEntity(it.packageName, it.usageTime, challengeDate) }
    )
}

internal fun List<ChallengeWithUsageInput>.toRequestChallengeWithUsage(): ChallengeWithUsageRequest {
    return ChallengeWithUsageRequest(
        finishedDailyChallenges = map {
            ChallengeWithUsageRequest.DailyChallenges(
                challengeDate = it.challengeDate,
                apps = it.apps.map { app ->
                    ChallengeWithUsageRequest.DailyChallenges.AppUsage(
                        appCode = app.packageName,
                        usageTime = app.usageTime
                    )
                }
            )
        }
    )
}

internal fun UsageEntity.toUsage(): ChallengeWithUsageInput.Usage {
    return ChallengeWithUsageInput.Usage(
        packageName = packageName,
        usageTime = usageTime
    )
}