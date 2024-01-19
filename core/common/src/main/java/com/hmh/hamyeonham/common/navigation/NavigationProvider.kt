package com.hmh.hamyeonham.common.navigation

import android.content.Intent

interface NavigationProvider {
    companion object{
        const val UN_LOCK_PACKAGE_NAME = "UN_LOCK_PACKAGE_NAME"
    }
    fun toOnBoarding(): Intent
    fun toLogin(): Intent
    fun toMain(): Intent
    fun toLock(packageName:String): Intent
}
