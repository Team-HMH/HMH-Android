package com.hmh.hamyeonham.login.datasource

import com.hmh.hamyeonham.login.di.AuthProvider
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AuthDataSourceFactory @Inject constructor(
  private val sources: Map<AuthProvider, @JvmSuppressWildcards AuthDataSource>
) {
  fun get(provider: AuthProvider): AuthDataSource =
    sources[provider] ?: error("No AuthDataSource bound for $provider")
}