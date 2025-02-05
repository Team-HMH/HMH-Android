package com.hmh.hamyeonham.core.network.auth.authenticator

import android.content.Context
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.core.database.manger.DatabaseManager
import com.hmh.hamyeonham.core.network.auth.datastore.network.HMHNetworkPreference
import com.jakewharton.processphoenix.ProcessPhoenix
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class AuthenticatorUtil @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: HMHNetworkPreference,
    private val databaseManager: DatabaseManager,
    private val navigationProvider: NavigationProvider,
) {
    suspend fun handleLogout() = suspendCancellableCoroutine { continuation ->
        dataStore.clear()
        databaseManager.deleteAll()
        UserApiClient.instance.logout { error ->
            runBlocking {
                Timber.tag("Authenticator").e("Logout error: $error")
                ProcessPhoenix.triggerRebirth(context, navigationProvider.toLogin())
                delay(500)
                continuation.resume(Unit)
            }
        }
    }

}