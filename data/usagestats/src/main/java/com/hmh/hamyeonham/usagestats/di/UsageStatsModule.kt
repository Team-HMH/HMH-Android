package com.hmh.hamyeonham.usagestats.di

import android.app.usage.UsageStatsManager
import android.content.Context
import com.hmh.hamyeonham.usagestats.datasource.UsageStatsDataSource
import com.hmh.hamyeonham.usagestats.datasource.UsageStatsDataSourceImpl
import com.hmh.hamyeonham.usagestats.repository.DefaultUsageGoalsRepository
import com.hmh.hamyeonham.usagestats.repository.DefaultUsageStatsRepository
import com.hmh.hamyeonham.usagestats.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.repository.UsageStatsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsageStatsModule {

    @Provides
    @Singleton
    fun provideUsageStatusManager(@ApplicationContext context: Context): UsageStatsManager? {
        return context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface Binder {
        @Binds
        @Singleton
        fun provideUsageStatusDataSource(usageStatsDataSource: UsageStatsDataSourceImpl): UsageStatsDataSource

        @Binds
        @Singleton
        fun provideUsageStatusRepository(usageStatsRepository: DefaultUsageStatsRepository): UsageStatsRepository
    }
}
