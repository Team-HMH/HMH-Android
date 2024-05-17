package com.hmh.hamyeonham.data.point.di

import com.hmh.hamyeonham.data.point.repository.DefaultPointRepository
import com.hmh.hamyeonham.domain.point.repository.PointRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PointBinder {
    @Binds
    @Singleton
    fun bindPointRepository(repository: DefaultPointRepository): PointRepository
}