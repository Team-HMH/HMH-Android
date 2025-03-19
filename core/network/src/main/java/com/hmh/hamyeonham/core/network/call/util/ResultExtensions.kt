package com.hmh.hamyeonham.core.network.call.util

import com.hmh.hamyeonham.core.network.call.ApiException
import com.hmh.hamyeonham.core.network.model.BaseResponse
import com.hmh.hamyeonham.core.network.model.ErrorResponse
import retrofit2.HttpException

fun <T> Result<BaseResponse<T>>.getResult(): Result<T> {
    return this.fold(
        onSuccess = { response ->
            response.data?.let { Result.success(it) }
                ?: Result.failure(IllegalStateException("Response body is null"))
        },
        onFailure = { throwable ->
            val errorResponse = when (throwable) {
                is HttpException -> {
                    val errorBody = throwable.response()?.errorBody()?.string()
                    ErrorResponse(
                        status = throwable.code(),
                        message = errorBody ?: "Unknown server error"
                    )
                }
                else -> ErrorResponse(
                    status = -1,
                    message = throwable.message ?: "Unknown error occurred"
                )
            }
            Result.failure(ApiException(errorResponse))
        }
    )
}
