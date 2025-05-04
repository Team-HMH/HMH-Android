package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.login.di.AuthProvider
import javax.inject.Inject

class DefaultAuthRepositorySelector @Inject constructor(
    private val factory: AuthRepositoryFactory
) : AuthRepositorySelector {
    override fun getRepository(provider: AuthProvider): AuthRepository {
        return factory.createLocalRepository(provider)
    }
}