package com.hmh.hamyeonham.core.network.auth.datastore.network

interface UserPreference {
    var accessToken: String
    var refreshToken: String
    var userName: String
    var userId: Long
    var autoLoginConfigured: Boolean
    fun clear()
}
