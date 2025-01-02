package com.hmh.hamyeonham.core.network.call

class ApiException(
    override val message: String,
    throwable: Throwable
): Exception(message, throwable)