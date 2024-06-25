package com.hmh.hamyeonham.login.di

import com.hmh.hamyeonham.login.DefaultAuthRepository
import com.hmh.hamyeonham.login.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Module
    @InstallIn(SingletonComponent::class)
    interface Binder {
        @Binds
        @Singleton
        fun provideUsageGoalsRepository(loginRepository: DefaultAuthRepository): AuthRepository
    }
}
