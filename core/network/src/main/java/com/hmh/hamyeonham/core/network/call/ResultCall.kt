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
    private val call: Call<BaseResponse<T>>,
    private val retrofit: Retrofit,
): Call<Result<T>>{

    // enqueue() : HTTP 요청을 비동기적으로 실행. 요청 결과를 전달받기 위해 Callback<Result<T>>를 인자로 받음
    // enqueue() 메서드를 Custom
    override fun enqueue(callback: Callback<Result<T>>) {

        // Callback<Result<T>> 객체를 인자로 받아 Call 객체의 enqueue() 메서드 호출
        call.enqueue(object : Callback<BaseResponse<T>> {

            // onResponse() : HTTP 요청 성공 시 호출
            override fun onResponse(call: Call<BaseResponse<T>>, response: Response<BaseResponse<T>>) {
                val responseBody = response.body()

                // response.isSuccessful : HTTP 응답 코드가 200~299 사이인지 여부 반환(성공 여부)
                if (response.isSuccessful) {
                    // HTTP 요청은 성공했지만 body가 빈 경우
                    if(responseBody == null) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.failure(RuntimeException("응답 body가 비었습니다.")))
                        )
                    } else { // HTTP 요청 성공
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.success(responseBody.data))
                        )
                    }
                } else { // HTTP 요청 실패
                    val errorResponse = try {
                        retrofit.responseBodyConverter<ErrorResponse>(
                            ErrorResponse::class.java,
                            ErrorResponse::class.java.annotations
                        ).convert(response.errorBody()!!)
                    } catch (e: Exception) {
                        null
                    } ?: getBodyNullErrorResponse(response)

                    callback.onResponse(
                        this@ResultCall,
                        Response.success(Result.failure(ApiException(errorResponse)))
                    )

                    Timber.tag("ResultCall - onResponse").e("ErrorResponse: $errorResponse")
                }
            }

            // onFailure() : HTTP 요청 실패 시 호출
            override fun onFailure(call: Call<BaseResponse<T>>, t: Throwable) {
                val message = when (t) {
                    is IOException -> "네트워크 연결이 불안정합니다. 다시 시도해주세요."
                    is HttpException -> "${t.code()} : 서버 통신 중 문제가 발생했습니다. 다시 시도해주세요."
                    else -> t.message ?: "알 수 없는 오류가 발생했습니다. 다시 시도해주세요."
                }

                // ApiException 객체 생성
                callback.onResponse(
                    this@ResultCall,
                    Response.success(Result.failure(RuntimeException(message)))
                )

                Timber.tag("ResultCall - onFailure").e("onFailure: $message")
            }
        })
    }

    // 응답 Body가 null인 경우 임시 ErrorResponse 객체 생성
    private fun getBodyNullErrorResponse(response: Response<BaseResponse<T>>) = ErrorResponse(
        status = response.code(),
        message = "response body is null"
    )

    // clone() : 동일 요청 수행하는 새로운 Call 객체 반환
    override fun clone(): Call<Result<T>> {
        return ResultCall(call.clone(), retrofit)
    }

    // execute() : HTTP 요청을 동기적으로 실행. 성공 시 Response<Result<T>> 반환, 실패 시 Exception 발생
    // 주로 테스트 용도
    override fun execute(): Response<Result<T>> {
        return Response.success(Result.failure(RuntimeException("execute() 메서드는 사용할 수 없습니다.")))
    }

    // isExecuted() : HTTP 요청이 이미 실행되었는지 여부 반환.
    // 한 번 이상 실행되면 true, 그렇지 않으면 false 반환
    override fun isExecuted(): Boolean {
        return call.isExecuted
    }

    // cancel() : 실행 중인 HTTP 요청 취소
    override fun cancel() {
        call.cancel()
    }

    // isCanceled() : HTTP 요청이 취소되었는지 여부 반환
    override fun isCanceled(): Boolean {
        return call.isCanceled
    }

    // request() : Call 객체에 대한 HTTP 요청 반환
    override fun request(): Request {
        return call.request()
    }

    // timeout() : Call 객체에 대한 Timeout 객체 반환
    override fun timeout(): Timeout {
        return call.timeout()
    }
}