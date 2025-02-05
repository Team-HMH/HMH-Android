package com.hmh.hamyeonham.core.network.auth.interceptor

import com.hmh.hamyeonham.core.network.auth.authenticator.AuthenticatorUtil
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authenticatorUtil: AuthenticatorUtil
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code == 404) {
            val responseBody = response.body?.string() ?: ""
            if (responseBody.contains("유저를 찾을 수 없습니다.")) {
                runBlocking {
                    authenticatorUtil.handleLogout()
                }

            }
        }

        return response
    }
}