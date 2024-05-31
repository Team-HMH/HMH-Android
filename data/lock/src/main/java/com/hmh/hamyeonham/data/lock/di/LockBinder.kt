package com.hmh.hamyeonham.data.lock.di

import com.hmh.hamyeonham.data.lock.DefaultLockRepository
import com.hmh.hamyeonham.lock.LockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LockBinder {

    @Binds
    @Singleton
    fun bind(lockRepository: DefaultLockRepository): LockRepository
}