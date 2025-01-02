package com.hmh.hamyeonham.core.network.call

import com.hmh.hamyeonham.core.network.model.BaseResponse
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
): Call<Result<T>>{

    // enqueue() : HTTP 요청을 비동기적으로 실행. 요청 결과를 전달받기 위해 Callback<Result<T>>를 인자로 받음
    // enqueue() 메서드를 Custom
    override fun enqueue(callback: Callback<Result<T>>) {

        // Callback<Result<T>> 객체를 인자로 받아 Call 객체의 enqueue() 메서드 호출
        call.enqueue(object : Callback<T> {

            // onResponse() : HTTP 요청 성공 시 호출
            override fun onResponse(call: Call<T>, response: Response<T>) {

                // response.isSuccessful : HTTP 응답 코드가 200~299 사이인지 여부 반환(성공 여부)
                if (response.isSuccessful) {

                    // HTTP 요청은 성공했지만 body가 빈 경우
                    if(response.body() == null) {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(Result.failure(ApiException("응답 body가 비었습니다.", HttpException(response))))
                        )
                    }
                    // HTTP 요청 성공
                    else {
                        callback.onResponse(
                            this@ResultCall,
                            Response.success(response.code(), Result.success(response.body() ?: throw NullPointerException("응답 body가 비었습니다.")))
                        )
                    }

                } else { // HTTP 요청 실패

                    // errorBody가 비어있는 경우
                    if(response.errorBody() == null) {
                        callback.onResponse( this@ResultCall,
                            Response.success(Result.failure(ApiException("errorBody가 비었습니다.", HttpException(response))))
                        )
                    }
                    // 요청에 실패한 경우
                    else {
                        // errorBody에서 사용자에게 보여줄 메시지 추출
                        // errorBody를 BaseResponse 객체로 변환
                        val errorBody = retrofit.responseBodyConverter<BaseResponse<Unit>>(
                            BaseResponse::class.java,
                            BaseResponse::class.java.annotations
                        ).convert(response.errorBody() ?: throw NullPointerException("errorBody가 비었습니다."))

                        val message: String = errorBody?.message ?: "errorBody가 비었습니다"

                        // ApiException 객체 생성
                        callback.onResponse(this@ResultCall,
                            Response.success(Result.failure(ApiException(message, HttpException(response))))
                        )

                        Timber.tag("ResultCall - onResponse").e("${ApiException(message, HttpException(response))}")
                    }
                }
            }

            // onFailure() : HTTP 요청 실패 시 호출
            override fun onFailure(call: Call<T>, t: Throwable) {
                val message = when (t) {
                    is IOException -> "네트워크 연결이 불안정합니다. 다시 시도해주세요."
                    is HttpException -> "${t.code()} : 서버 통신 중 문제가 발생했습니다. 다시 시도해주세요."
                    else -> t.message ?: "알 수 없는 오류가 발생했습니다. 다시 시도해주세요."
                }

                // ApiException 객체 생성
                callback.onResponse(
                    this@ResultCall,
                    Response.success(Result.failure(ApiException(message, t)))
                )

                Timber.tag("ResultCall - onFailure").e("${ApiException(message, t)}")
            }
        })
    }

    // clone() : 동일 요청 수행하는 새로운 Call 객체 반환
    override fun clone(): Call<Result<T>> {
        return ResultCall(call.clone(), retrofit)
    }

    // execute() : HTTP 요청을 동기적으로 실행. 성공 시 Response<Result<T>> 반환, 실패 시 Exception 발생
    // 주로 테스트 용도
    override fun execute(): Response<Result<T>> {
        return Response.success(Result.success(call.execute().body() ?: throw NullPointerException("응답 body가 비었습니다.")))
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