package com.hmh.hamyeonham.login.di

import dagger.MapKey

@MapKey
@Retention(AnnotationRetention.BINARY)
annotation class AuthDataSourceKey(val value: AuthProvider)