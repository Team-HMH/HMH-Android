package com.hmh.hamyeonham.core.network.auth.interceptor

import android.content.Context
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.core.database.manger.DatabaseManager
import com.hmh.hamyeonham.core.network.auth.api.RefreshService
import com.hmh.hamyeonham.core.network.auth.authenticator.AuthenticatorUtil
import com.hmh.hamyeonham.core.network.auth.datastore.network.HMHNetworkPreference
import com.jakewharton.processphoenix.ProcessPhoenix
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
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