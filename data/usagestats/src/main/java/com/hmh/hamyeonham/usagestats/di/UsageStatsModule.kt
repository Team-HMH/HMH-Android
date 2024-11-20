package com.hmh.hamyeonham.usagestats.di

import android.app.usage.UsageStatsManager
import android.content.Context
import com.hmh.hamyeonham.core.domain.usagegoal.repository.UsageGoalsRepository
import com.hmh.hamyeonham.usagestats.datasource.local.DeletedAppUsageLocalDataSource
import com.hmh.hamyeonham.usagestats.datasource.local.DeletedAppUsageLocalDataSourceImpl
import com.hmh.hamyeonham.usagestats.repository.DefaultDeleteGoalRepository
import com.hmh.hamyeonham.usagestats.repository.DefaultUsageGoalsRepository
import com.hmh.hamyeonham.usagestats.repository.DefaultUsageStatsRepository
import com.hmh.hamyeonham.usagestats.repository.DeleteGoalRepository
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
        fun provideDeletedAppUsageDataSource(deletedAppUsageLocalDataSource: DeletedAppUsageLocalDataSourceImpl): DeletedAppUsageLocalDataSource

        @Binds
        @Singleton
        fun provideUsageStatsRepository(usageStatsRepository: DefaultUsageStatsRepository): UsageStatsRepository

        @Binds
        @Singleton
        fun provideUsageGoalsRepository(usageGoalsRepository: DefaultUsageGoalsRepository): UsageGoalsRepository

        @Binds
        @Singleton
        fun providesDeleteGoalsRepository(deleteGoalRepository: DefaultDeleteGoalRepository): DeleteGoalRepository

    }
}
