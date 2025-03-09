package com.hmh.hamyeonham.common.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Secured

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Unsecured

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ResultCall
