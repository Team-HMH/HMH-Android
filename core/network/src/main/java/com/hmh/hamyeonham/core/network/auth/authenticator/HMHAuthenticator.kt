package com.hmh.hamyeonham.core.network.auth.authenticator

import android.content.Context
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.core.database.manger.DatabaseManager
import com.hmh.hamyeonham.core.network.auth.api.RefreshService
import com.hmh.hamyeonham.core.network.auth.datastore.network.HMHNetworkPreference
import com.jakewharton.processphoenix.ProcessPhoenix
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HMHAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: HMHNetworkPreference,
    private val api: RefreshService,
    private val databaseManager: DatabaseManager,
    private val navigationProvider: NavigationProvider,
) : Authenticator {
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        val originalRequest = response.request
        if (originalRequest.header("Authorization") == null) {
            return null
        }

        // 401 상태 처리
        if (response.code == 401) {
            return runBlocking {
                mutex.withLock {
                    try {
                        // 토큰 갱신 호출
                        val refreshToken = dataStore.refreshToken
                        val newTokens = api.refreshToken("Bearer $refreshToken").data

                        // 토큰 저장
                        dataStore.accessToken = newTokens.accessToken.orEmpty()
                        dataStore.refreshToken = newTokens.refreshToken.orEmpty()

                        // 새로운 요청 반환
                        originalRequest.newBuilder()
                            .header("Authorization", "Bearer ${newTokens.accessToken}")
                            .build()
                    } catch (e: Exception) {
                        Timber.tag("Authenticator").e("Token refresh failed: ${e.message}")
                        handleLogout()
                        null
                    }
                }
            }
        }
        return null
    }

    private fun handleLogout() {
        runBlocking {
            dataStore.clear()
            databaseManager.deleteAll()
            UserApiClient.instance.logout { error ->
                Timber.tag("Authenticator").e("Logout error: $error")
                ProcessPhoenix.triggerRebirth(context, navigationProvider.toLogin())
            }
        }
    }
}