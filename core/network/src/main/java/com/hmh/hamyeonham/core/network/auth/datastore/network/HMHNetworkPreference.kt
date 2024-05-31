package com.hmh.hamyeonham.core.network.auth.datastore.network

interface HMHNetworkPreference {
    var accessToken: String
    var refreshToken: String
    var userName: String
    var userId: Int
    var autoLoginConfigured: Boolean
    var isUnlock: Boolean
    fun clear()
}
