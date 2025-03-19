package com.hmh.hamyeonham.core.network.di

import com.hmh.hamyeonham.common.BuildConfig
import com.hmh.hamyeonham.common.qualifier.Authenticated
import com.hmh.hamyeonham.common.qualifier.Header
import com.hmh.hamyeonham.common.qualifier.Log
import com.hmh.hamyeonham.common.qualifier.Secured
import com.hmh.hamyeonham.common.qualifier.Unsecured
import com.hmh.hamyeonham.core.network.auth.authenticator.AuthenticatorUtil
import com.hmh.hamyeonham.core.network.auth.authenticator.HMHAuthenticator
import com.hmh.hamyeonham.core.network.auth.interceptor.AuthInterceptor
import com.hmh.hamyeonham.core.network.auth.interceptor.HeaderInterceptor
import com.hmh.hamyeonham.core.network.call.ResultCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val HMHBaseUrl = BuildConfig.HMH_BASE_URL

@Module
@InstallIn(SingletonComponent::class)
object NetModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Singleton
    @Provides
    fun provideJsonConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Singleton
    @Provides
    @Log
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            },
        )

    @Singleton
    @Provides
    @Header
    fun provideHeaderInterceptor(interceptor: HeaderInterceptor): Interceptor = interceptor

    @Singleton
    @Provides
    @Authenticated
    fun provideAuthInterceptor(
        authenticatorUtil: AuthenticatorUtil
    ): AuthInterceptor = AuthInterceptor(authenticatorUtil)

    @Singleton
    @Provides
    @Secured
    fun provideOkHttpClient(
        @Log logInterceptor: Interceptor,
        @Header headerInterceptor: Interceptor,
        @Authenticated authInterceptor: AuthInterceptor,
        authenticator: Authenticator,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logInterceptor)
        .addInterceptor(headerInterceptor)
        .authenticator(authenticator)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    @Unsecured
    fun provideOkHttpClientNotNeededAuth(
        @Log logInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideResultCallAdapterFactory(): ResultCallAdapterFactory = ResultCallAdapterFactory()

    @Singleton
    @Provides
    @Secured
    fun provideRetrofit(
        @Secured client: OkHttpClient,
        converterFactory: Converter.Factory,
        resultCallAdapterFactory: ResultCallAdapterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(HMHBaseUrl)
        .client(client)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(resultCallAdapterFactory)
        .build()

    @Singleton
    @Provides
    @Unsecured
    fun provideRetrofitNotNeededAuth(
        @Unsecured client: OkHttpClient,
        converterFactory: Converter.Factory,
        resultCallAdapterFactory: ResultCallAdapterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(HMHBaseUrl)
        .client(client)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(resultCallAdapterFactory)
        .build()

    @Module
    @InstallIn(SingletonComponent::class)
    interface Binder {
        @Binds
        @Singleton
        fun provideAuthenticator(authenticator: HMHAuthenticator): Authenticator
    }
}
