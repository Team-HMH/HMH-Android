package com.hmh.hamyeonham.data.challenge.di

import com.hmh.hamyeonham.challenge.repository.ChallengeRepository
import com.hmh.hamyeonham.data.challenge.datasource.ChallengeLocalDataSourceImpl
import com.hmh.hamyeonham.data.challenge.datasource.ChallengeLocalDatasource
import com.hmh.hamyeonham.data.challenge.repository.DefaultChallengeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChallengeModule {
    @Module
    @InstallIn(SingletonComponent::class)
    interface Binder {
        @Binds
        @Singleton
        fun bindChallengeDataSource(challengeLocalDataSourceImpl: ChallengeLocalDataSourceImpl): ChallengeLocalDatasource

        @Binds
        @Singleton
        fun bindChallengeRepository(challengeRepository: DefaultChallengeRepository): ChallengeRepository
    }
}
