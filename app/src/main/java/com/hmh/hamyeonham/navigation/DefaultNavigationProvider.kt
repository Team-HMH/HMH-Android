package com.hmh.hamyeonham.navigation

import android.content.Context
import android.content.Intent
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.feature.login.LoginActivity
import com.hmh.hamyeonham.feature.login.UserInfoActivity
import com.hmh.hamyeonham.feature.main.MainActivity
import com.hmh.hamyeonham.feature.onboarding.OnBoardingActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultNavigationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : NavigationProvider {
    override fun toOnboarding(): Intent {
        return Intent(context, OnBoardingActivity::class.java)
    }

    override fun toLogin(): Intent {
        return Intent(context, LoginActivity::class.java)
    }

    override fun toUserInfo(): Intent {
        return Intent(context, UserInfoActivity::class.java)
    }

    override fun toMain(): Intent {
        return Intent(context, MainActivity::class.java)
    }
}
