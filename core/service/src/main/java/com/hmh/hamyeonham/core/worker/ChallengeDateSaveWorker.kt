package com.hmh.hamyeonham.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hmh.hamyeonham.challenge.model.ChallengeWithUsage
import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.common.time.getYesterdayDateNumeric
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

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
            val workRequest = OneTimeWorkRequestBuilder<ChallengeDateSaveWorker>().build()

            // WorkManager를 사용하여 작업 예약
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    override suspend fun doWork(): Result {
        val challengeWithUsage = ChallengeWithUsage(
            challengeDate = getYesterdayDateNumeric(),
            apps = emptyList(),
        )
        return runCatching {
            challengeRepository.insertChallengeWithUsage(challengeWithUsage)
        }.fold(
            onSuccess = {
                Result.success()
            },
            onFailure = { Result.retry() }
        )
    }
}