package com.hmh.hamyeonham.core.database.di

import com.hmh.hamyeonham.core.database.HMHRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun providesUsageGoalsDao(
        database: HMHRoomDatabase,
    ) = database.usageGoalsDao()

    @Provides
    @Singleton
    fun providesUsageTotalGoalDao(
        database: HMHRoomDatabase,
    ) = database.usageTotalGoalDao()

    @Provides
    @Singleton
    fun providesChallengeDao(
        database: HMHRoomDatabase,
    ) = database.challengeDao()

    @Provides
    @Singleton
    fun providesDeletedGoalDao(
        database: HMHRoomDatabase,
    ) = database.deletedGoalsDao()

    @Provides
    @Singleton
    fun providesLockDao(
        database: HMHRoomDatabase,
    ) = database.lockDao()
}
