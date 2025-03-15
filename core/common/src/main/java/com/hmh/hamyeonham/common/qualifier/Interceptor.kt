package com.hmh.hamyeonham.common.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Log

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Header

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Authenticated
