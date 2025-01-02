package com.hmh.hamyeonham.core.network.call

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

// CallAdapter 객체 생성을 위한 Factory 클래스
class ResultCallAdapterFactory @Inject constructor(): CallAdapter.Factory() {

    // retrofit 호출 결과를 변환하기 위해 호출
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // 변환 타입 검증 (기본 Type이 Call인지, 제네릭 타입인지)
        if (getRawType(returnType) != Call::class.java || returnType !is ParameterizedType) {
            return null
        }

        // Retrofit의 반환 타입이 Call<Result<*>>인지 확인
        val upperBound = getParameterUpperBound(0, returnType)

        return if (upperBound is ParameterizedType && upperBound.rawType == Result::class.java) {
            object : CallAdapter<Any, Call<Result<*>>> {
                override fun responseType(): Type = getParameterUpperBound(0, upperBound)

                // 기존 Call 객체를 ResultCall 객체로 변환
                override fun adapt(call: Call<Any>): Call<Result<*>> =
                    ResultCall(call, retrofit) as Call<Result<*>>
            }
        } else {
            // 반환 객체가 Call<Result<*>>이 아닌 경우 null 반환
            null
        }
    }
}