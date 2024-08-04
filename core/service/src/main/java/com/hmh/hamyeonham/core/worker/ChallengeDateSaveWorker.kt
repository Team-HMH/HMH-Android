package com.hmh.hamyeonham.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsageInput
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.getYesterdayDateNumeric
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class ChallengeDateSaveWorker @AssistedInject constructor(
    @Assisted private val applicationContext: Context,
    @Assisted params: WorkerParameters,
    private val challengeRepository: ChallengeRepository,
) : CoroutineWorker(applicationContext, params) {

    companion object {
        private const val TAG = "ChallengeDateSaveWorker"
        fun runAt(context: Context) {
            // 작업 요청 생성
            val workRequest = OneTimeWorkRequestBuilder<ChallengeDateSaveWorker>()
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    override suspend fun doWork(): Result {
        val challengeWithUsageInput = ChallengeWithUsageInput(
            challengeDate = getYesterdayDateNumeric(),
            apps = emptyList(),
        )
        return runCatching {
            challengeRepository.insertChallengeWithUsage(challengeWithUsageInput)
        }.fold(
            onSuccess = {
                Result.success()
            },
            onFailure = { Result.retry() }
        )
    }
}