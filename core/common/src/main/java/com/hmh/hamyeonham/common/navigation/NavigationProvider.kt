package com.hmh.hamyeonham.common.navigation

import android.content.Intent
import androidx.fragment.app.Fragment

interface NavigationProvider {
    companion object {
        const val UN_LOCK_PACKAGE_NAME = "UN_LOCK_PACKAGE_NAME"
    }

    fun toOnBoarding(): Intent
    fun toOnBoardingStory(): Intent
    fun toLogin(): Intent
    fun toMain(): Intent
    fun toLock(packageName: String): Intent
    fun toStore(): Intent
    fun toPoint(): Intent
    fun toPermission(): Fragment
}
