package com.hmh.hamyeonham.data.challenge.mapper

import com.hmh.hamyeonham.challenge.model.ChallengeStatus
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsage
import com.hmh.hamyeonham.core.database.model.ChallengeWithUsageEntity
import com.hmh.hamyeonham.core.database.model.DailyChallengeEntity
import com.hmh.hamyeonham.core.database.model.UsageEntity
import com.hmh.hamyeonham.core.network.challenge.model.ChallengeResponse
import com.hmh.hamyeonham.core.network.usagegoal.model.ChallengeWithUsageRequest
import com.hmh.hamyeonham.core.network.usagegoal.model.UsageGoalResponse
import com.hmh.hamyeonham.usagestats.model.UsageStatus

internal fun ChallengeResponse.toChallengeStatus(): ChallengeStatus {
    return ChallengeStatus(
        apps.map {
            ChallengeStatus.AppGoal(it.appCode, it.goalTime)
        },
        statuses.toStatusList(todayIndex),
        goalTime,
        period,
        todayIndex,
    )
}

internal fun UsageGoalResponse.toChallengeResult(): Boolean {
    return when (status) {
        ChallengeStatus.Status.NONE.value -> true
        //FAIL일 경우
        else -> false
    }
}

internal fun ChallengeWithUsageEntity.toChallengeWithUsage(): ChallengeWithUsage {
    return ChallengeWithUsage(
        challengeDate = challenge.challengeDate,
        isUnlock = challenge.isUnlock,
        apps = apps.map { it.toUsage() }
    )
}

internal fun ChallengeWithUsage.toChallengeWithUsageEntity(): ChallengeWithUsageEntity {
    return ChallengeWithUsageEntity(
        challenge = DailyChallengeEntity(
            challengeDate = challengeDate,
            isUnlock = isUnlock
        ),
        apps = apps.map { UsageEntity(it.packageName, it.usageTime, challengeDate) }
    )
}

internal fun List<ChallengeWithUsage>.toRequestChallengeWithUsage(): ChallengeWithUsageRequest {
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

internal fun UsageEntity.toUsage(): ChallengeWithUsage.Usage {
    return ChallengeWithUsage.Usage(
        packageName = packageName,
        usageTime = usageTime
    )
}

internal fun UsageStatus.toUsage(): ChallengeWithUsage.Usage {
    return ChallengeWithUsage.Usage(packageName, totalTimeInForeground)
}
