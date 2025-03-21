package com.hmh.hamyeonham

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.amplitude.api.Amplitude
import com.hmh.hamyeonham.core.notification.AppNotificationManager
import com.hmh.hamyeonham.firebase.setFirebaseCrashlyticsEnabled
import com.hmh.hamyeonham.hus.usagestats.HMHUsageStatsManager
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@HiltAndroidApp
class HMHApplication : Application(), Configuration.Provider {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }

    @Inject
    lateinit var notificationManager: AppNotificationManager

    override val workManagerConfiguration: Configuration = Configuration.Builder()
        .setWorkerFactory(
            EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory(),
        ).build()

    override fun onCreate() {
        super.onCreate()
        setFirebaseCrashlyticsEnabled(!BuildConfig.DEBUG)
        setAmplitude()
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        HMHUsageStatsManager.init(this)
        notificationManager.setupNotificationChannel()
    }

    private fun setAmplitude() {
        Amplitude
            .getInstance()
            .initialize(this, BuildConfig.AMPLITUDE_API_KEY)
            .enableForegroundTracking(this)
            .enableLogging(true)
    }
}
