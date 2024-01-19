package com.hmh.hamyeonham.core.network.di

import com.hmh.hamyeonham.common.qualifier.Secured
import com.hmh.hamyeonham.core.network.usagegoal.DailyChallengeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsageGoalModule {
    @Provides
    @Singleton
    fun provideUsageGoalApi(@Secured retrofit: Retrofit): DailyChallengeService = retrofit.create()
}
