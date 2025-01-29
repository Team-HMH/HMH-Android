package com.hmh.hamyeonham.feature.lock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hmh.hamyeonham.common.app.killAppByPackageName
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.core.designsystem.ui.theme.HMHAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LockActivity : ComponentActivity() {
    @Inject
    lateinit var navigationProvider: NavigationProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageName = intent.getStringExtra(PACKAGE_NAME).orEmpty()
        setContent {
            HMHAndroidTheme {
                LockScreen(
                    packageName = packageName,
                    onClickClose = {
                        killAppByPackageName(
                            context = this,
                            packageName = packageName,
                        )
                        finish()
                    },
                    onClickUnLock = {
                        navigationProvider.toMain().apply {
                            putExtra(NavigationProvider.UN_LOCK_PACKAGE_NAME, packageName)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        }.let(::startActivity)
                        finish()
                    },
                )
            }
        }
    }

    companion object {
        private const val PACKAGE_NAME = "PACKAGE_NAME"
        fun getIntent(context: Context, packageName: String): Intent {
            return Intent(context, LockActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra(PACKAGE_NAME, packageName)
            }
        }
    }
}
