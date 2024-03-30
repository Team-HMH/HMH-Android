package com.hmh.hamyeonham.navigation

import android.content.Context
import android.content.Intent
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.feature.lock.LockActivity
import com.hmh.hamyeonham.feature.login.LoginActivity
import com.hmh.hamyeonham.feature.main.MainActivity
import com.hmh.hamyeonham.feature.onboarding.OnBoardingActivity
import com.hmh.hamyeonham.feature.onboarding.OnBoardingStoryActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultNavigationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : NavigationProvider {
    override fun toOnBoarding(): Intent {
        return Intent(context, OnBoardingActivity::class.java)
    }

    override fun toOnBoardingStory(): Intent {
        return Intent(context, OnBoardingStoryActivity::class.java)
    }
    override fun toLogin(): Intent {
        return Intent(context, LoginActivity::class.java)
    }

    override fun toMain(): Intent {
        return Intent(context, MainActivity::class.java)
    }

    override fun toLock(packageName: String): Intent {
        return LockActivity.getIntent(context, packageName)
    }
}
