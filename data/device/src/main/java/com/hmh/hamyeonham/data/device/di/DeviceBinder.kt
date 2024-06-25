package com.hmh.hamyeonham.data.device.di

import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import com.hmh.hamyeonham.data.device.repository.DefaultDeviceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DeviceBinder {
    @Binds
    @Singleton
    fun bind(deviceRepository: DefaultDeviceRepository): DeviceRepository
}
