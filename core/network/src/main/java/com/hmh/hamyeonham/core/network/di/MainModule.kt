package com.hmh.hamyeonham.core.network.di

import com.hmh.hamyeonham.common.qualifier.ResultCall
import com.hmh.hamyeonham.core.network.main.MainService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideMainService(@ResultCall retrofit: Retrofit): MainService = retrofit.create()
}
