package com.hmh.hamyeonham.core.network.call

import com.hmh.hamyeonham.core.network.model.BaseResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class ResultCallAdapterFactory @Inject constructor() : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // 서비스 인터페이스의 반환 타입이 Call<Result<*>>여야 함
        if (getRawType(returnType) != Call::class.java || returnType !is ParameterizedType) {
            return null
        }

        // 내부의 Result<T> 형태여야 함
        val resultType = getParameterUpperBound(0, returnType)
        if (getRawType(resultType) != Result::class.java || resultType !is ParameterizedType) {
            return null
        }

        // Result<T>에서 T를 추출
        val innerType = getParameterUpperBound(0, resultType)

        return object : CallAdapter<Any, Call<Result<*>>> {
            override fun responseType(): Type {
                // Retrofit에게 BaseResponse<T>로 변환하도록 타입을 전달합니다.
                return newParameterizedType(BaseResponse::class.java, innerType)
            }

            override fun adapt(call: Call<Any>): Call<Result<*>> {
                return ResultCall(call, retrofit) as Call<Result<*>>
            }
        }
    }

    private fun newParameterizedType(rawType: Type, vararg typeArguments: Type): ParameterizedType {
        return object : ParameterizedType {
            override fun getRawType(): Type = rawType
            override fun getOwnerType(): Type? = null
            override fun getActualTypeArguments(): Array<out Type> = typeArguments
        }
    }
}