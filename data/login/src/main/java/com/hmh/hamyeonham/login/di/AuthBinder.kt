package com.hmh.hamyeonham.login.di

import com.hmh.hamyeonham.login.repository.DefaultAuthRepository
import com.hmh.hamyeonham.login.repository.LocalAuthRepository
import com.hmh.hamyeonham.login.datasource.AuthDataSource
import com.hmh.hamyeonham.login.datasource.kakao.KakaoAuthDataSourceImpl
import com.hmh.hamyeonham.login.repository.AuthRepository
import com.hmh.hamyeonham.login.repository.AuthRepositorySelector
import com.hmh.hamyeonham.login.repository.DefaultAuthRepositorySelector
import com.hmh.hamyeonham.login.repository.LocalAuth
import com.hmh.hamyeonham.login.repository.RemoteAuth
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthBinder {
    @Binds
    @Singleton
    @RemoteAuth
    fun bindDefaultAuthRepository(loginRepository: DefaultAuthRepository): AuthRepository

    @Binds
    @Singleton
    @LocalAuth
    fun bindLocalAuthRepository(
        localAuthRepository: LocalAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    @IntoMap
    @AuthDataSourceKey(AuthProvider.KAKAO)
    fun bindKakaoAuthDataSource(kakaoAuthDataSourceImpl: KakaoAuthDataSourceImpl): AuthDataSource

    @Binds
    @Singleton
    fun bindAuthRepositorySelector(
        defaultAuthRepositorySelector: DefaultAuthRepositorySelector
    ): AuthRepositorySelector
}
