package com.hmh.hamyeonham.core.network.call.util

import com.hmh.hamyeonham.core.network.model.BaseResponse

fun <T> Result<BaseResponse<T>>.getResult(): Result<T> {
    return this.fold(
        onSuccess = { response ->
            response.data?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("response body is null"))
        },
        onFailure = { throwable ->
            Result.failure(throwable)
        }
    )
}