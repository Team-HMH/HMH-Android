package com.hmh.hamyeonham.core.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hmh.hamyeonham.usagestats.datastore.HMHDeletedAppUsagePreference
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.TimeUnit

@HiltWorker
class HMHDeletedAppUsagePreferenceResetWorker @AssistedInject constructor(
    @Assisted applicationContext: Context,
    @Assisted params: WorkerParameters,
    private val hmhDeletedAppUsagePreference: HMHDeletedAppUsagePreference
) : CoroutineWorker(applicationContext, params) {

    companion object {
        fun scheduleHMHDeletedAppUsagePreferenceResetWorker(context: Context) {
            // 현재 시간과 자정까지의 시간 계산
            val now = Clock.System.now()
            val currentSystemTimeZone = TimeZone.currentSystemDefault()
            val todayMidnight = now.toLocalDateTime(currentSystemTimeZone)
                .date
                .plus(1, DateTimeUnit.DAY)
                .atStartOfDayIn(currentSystemTimeZone)
            val initialDelay = todayMidnight.toEpochMilliseconds() - now.toEpochMilliseconds()

            // 주기적 작업 요청 생성
            val workRequest = PeriodicWorkRequestBuilder<HMHDeletedAppUsagePreferenceResetWorker>(
                1,
                TimeUnit.DAYS
            )
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()

            // WorkManager를 사용하여 작업 예약
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "HMHDeletedAppUsagePreferenceResetWorker",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
        }
    }

    override suspend fun doWork(): Result {
        hmhDeletedAppUsagePreference.clear()
        return Result.success()
    }
}
