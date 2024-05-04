package com.hmh.hamyeonham.challenge.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsage
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.getCurrentDateOfDefaultTimeZone
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.DateTimeUnit

@HiltWorker
class ChallengeDateSaveWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted params: WorkerParameters,
    private val challengeRepository: ChallengeRepository,
) : CoroutineWorker(applicationContext, params) {

    override suspend fun doWork(): Result {
        val currentDate = getCurrentDateOfDefaultTimeZone()
        val challengeWithUsage = ChallengeWithUsage(
            challengeDate = currentDate.toString(),
            apps = emptyList()
        )
        challengeRepository.insertChallengeWithUsage(challengeWithUsage)
        return Result.success()
    }
}