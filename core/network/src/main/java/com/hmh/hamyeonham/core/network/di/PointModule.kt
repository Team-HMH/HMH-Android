package com.hmh.hamyeonham.core.network.di

import com.hmh.hamyeonham.common.qualifier.Secured
import com.hmh.hamyeonham.core.network.point.PointService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PointModule {
    @Provides
    @Singleton
    fun providePointService(@Secured retrofit: Retrofit): PointService = retrofit.create()
}