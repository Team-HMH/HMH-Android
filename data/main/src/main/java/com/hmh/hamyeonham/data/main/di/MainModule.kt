package com.hmh.hamyeonham.data.main.di

import com.hmh.hamyeonham.data.main.DefaultMainRepository
import com.hmh.hamyeonham.domain.main.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Module
    @InstallIn(SingletonComponent::class)
    interface Binder {
        @Binds
        @Singleton
        fun provideUsageGoalsRepository(mainRepository: DefaultMainRepository): MainRepository
    }
}
