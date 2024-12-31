package com.hmh.hamyeonham.core.network.call

class CustomCallException(
    override val message: String,
    throwable: Throwable
): Exception(message, throwable)