package com.hmh.hamyeonham.core.network.di

import com.hmh.hamyeonham.common.qualifier.Secured
import com.hmh.hamyeonham.core.network.challenge.ChallengeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChallengeModule {
    @Provides
    @Singleton
    fun provideChallengeApi(@Secured retrofit: Retrofit): ChallengeService = retrofit.create()
}
