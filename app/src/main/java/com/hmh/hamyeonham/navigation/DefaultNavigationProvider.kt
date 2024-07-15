package com.hmh.hamyeonham.navigation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.hmh.hamyeonham.challenge.point.PointActivity
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.common.permission.PermissionActivity
import com.hmh.hamyeonham.feature.lock.LockActivity
import com.hmh.hamyeonham.feature.login.LoginActivity
import com.hmh.hamyeonham.feature.main.MainActivity
import com.hmh.hamyeonham.feature.onboarding.OnBoardingActivity
import com.hmh.hamyeonham.feature.onboarding.OnBoardingStoryActivity
import com.hmh.hamyeonham.feature.onboarding.fragment.OnBoardingRequestPermissionFragment
import com.hmh.hamyeonham.feature.store.StoreActivity
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

    override fun toStore(): Intent {
        return Intent(context, StoreActivity::class.java)
    }

    override fun toPoint(): Intent {
        return Intent(context, PointActivity::class.java)
    }

    override fun toPermission(): Intent {
        return Intent(context, PermissionActivity::class.java)
    }
}
