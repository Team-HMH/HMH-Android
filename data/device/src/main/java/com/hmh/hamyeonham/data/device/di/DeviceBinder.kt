package com.hmh.hamyeonham.data.device.di

import com.hmh.hamyeonham.challenge.repository.DeviceRepository
import com.hmh.hamyeonham.data.device.repository.DefaultDeviceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface DeviceBinder {
    @Binds
    @ViewModelScoped
    fun bind(deviceRepository: DefaultDeviceRepository): DeviceRepository
}
