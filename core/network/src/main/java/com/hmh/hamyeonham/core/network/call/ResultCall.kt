package com.hmh.hamyeonham.core.network.call

import com.hmh.hamyeonham.core.network.model.BaseResponse
import com.hmh.hamyeonham.core.network.model.ErrorResponse
import okhttp3.Request
import okio.IOException
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber

class ResultCall<T>(
    private val call: Call<T>,
    private val retrofit: Retrofit,
) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                val result = parseBaseResponse(response)
                callback.onResponse(this@ResultCall, Response.success(response.code(), result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val errorMessage = when (t) {
                    is IOException -> "네트워크 연결이 불안정합니다. 다시 시도해주세요."
                    is HttpException -> "${t.code()} : 서버 통신 중 문제가 발생했습니다. 다시 시도해주세요."
                    else -> t.message ?: "알 수 없는 오류가 발생했습니다. 다시 시도해주세요."
                }

                callback.onResponse(
                    this@ResultCall,
                    Response.success(Result.failure(RuntimeException(errorMessage)))
                )

                Timber.tag("ResultCall - onFailure").e("onFailure: $errorMessage")
            }
        })
    }

    override fun execute(): Response<Result<T>> {
        val response = call.execute()
        return Response.success(parseBaseResponse(response))
    }

    /**
     * BaseResponse<T>를 안전하게 처리하여 Result<T>로 변환
     */
    private fun parseBaseResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            val baseResponse = response.body() as? BaseResponse<T> ?: return Result.failure(
                ApiException(
                    ErrorResponse(
                        700,
                        "Response body is null or not in expected BaseResponse format"
                    )
                )
            )
            try {
                Result.success(baseResponse.data)
            } catch (e: Exception) {
                Result.failure(
                    ApiException(
                        ErrorResponse(
                            700,
                            "Response body conversion failed: ${e.message}"
                        )
                    )
                )
            }
        } else {
            val errorResponse = parseErrorResponse(response)
            Result.failure(ApiException(errorResponse))
        }
    }


    /**
     * Retrofit을 사용해 errorBody()를 파싱하여 ErrorResponse로 변환
     */
    private fun parseErrorResponse(response: Response<T>): ErrorResponse {
        return try {
            retrofit.responseBodyConverter<ErrorResponse>(
                ErrorResponse::class.java,
                ErrorResponse::class.java.annotations
            ).convert(response.errorBody()!!) ?: getBodyNullErrorResponse(response)
        } catch (e: Exception) {
            getBodyNullErrorResponse(response)
        }
    }

    /**
     * 응답 Body가 null인 경우 기본 ErrorResponse 반환
     */
    private fun getBodyNullErrorResponse(response: Response<T>) = ErrorResponse(
        status = response.code(),
        message = "response body is null"
    )

    override fun clone(): Call<Result<T>> = ResultCall(call.clone(), retrofit)

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() {
        call.cancel()
    }

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()
}
