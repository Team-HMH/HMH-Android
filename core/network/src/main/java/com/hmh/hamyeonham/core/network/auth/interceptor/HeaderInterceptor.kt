package com.hmh.hamyeonham.core.network.auth.interceptor

import com.hmh.hamyeonham.core.network.auth.datastore.network.UserPreference
import kotlinx.datetime.TimeZone
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val dataStore: UserPreference
) : Interceptor {
    private val encodedToken: String
        get() = "Bearer ${dataStore.accessToken}"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (!shouldRequestAuthenticatedHeaders(originalRequest.url.encodedPath)) {
            return chain.proceed(originalRequest)
        }
        val headerRequest = originalRequest.newBuilder()
            .addHeader("Authorization", encodedToken)
            .addHeader("OS", "Android")
            .addHeader("Time-Zone", TimeZone.currentSystemDefault().id)
            .build()
        return chain.proceed(headerRequest)
    }

    private fun shouldRequestAuthenticatedHeaders(encodedPath: String) = when (encodedPath) {
        "/api/v1/user/reissue" -> false
        else -> true
    }
}
