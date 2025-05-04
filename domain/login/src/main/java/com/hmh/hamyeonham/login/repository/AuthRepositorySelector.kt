package com.hmh.hamyeonham.login.repository

import com.hmh.hamyeonham.login.di.AuthProvider

interface AuthRepositorySelector {
    fun getRepository(provider: AuthProvider): AuthRepository
}