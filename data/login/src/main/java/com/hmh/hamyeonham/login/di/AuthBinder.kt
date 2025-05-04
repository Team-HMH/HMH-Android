package com.hmh.hamyeonham.login.di

import com.hmh.hamyeonham.login.datasource.kakao.KakaoSocialAuthDataStoreImpl
import com.hmh.hamyeonham.login.repository.AuthRepository
import com.hmh.hamyeonham.login.repository.LocalAuthRepository
import com.hmh.hamyeonham.login.repository.SocialAuthDataStore
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
    fun bindLocalAuthRepository(
        localAuthRepository: LocalAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    @IntoMap
    @AuthDataSourceKey(AuthProvider.KAKAO)
    fun bindKakaoSocialLoginRepositoryImpl(kakaoSocialLoginRepositoryImpl: KakaoSocialAuthDataStoreImpl): SocialAuthDataStore
}
