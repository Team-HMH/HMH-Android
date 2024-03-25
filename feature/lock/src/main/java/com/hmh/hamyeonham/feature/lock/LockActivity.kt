package com.hmh.hamyeonham.feature.lock

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.common.context.toast
import com.hmh.hamyeonham.common.navigation.NavigationProvider
import com.hmh.hamyeonham.feature.lock.ui.theme.Blackground
import com.hmh.hamyeonham.feature.lock.ui.theme.BluePurpleButton
import com.hmh.hamyeonham.feature.lock.ui.theme.Gray1
import com.hmh.hamyeonham.feature.lock.ui.theme.Gray2
import com.hmh.hamyeonham.feature.lock.ui.theme.Gray3
import com.hmh.hamyeonham.feature.lock.ui.theme.HMHAndroidTheme
import com.hmh.hamyeonham.feature.lock.ui.theme.HmhTypography
import com.hmh.hamyeonham.feature.lock.ui.theme.WhiteBtn
import com.hmh.hamyeonham.feature.lock.ui.theme.WhiteText
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

@Composable
fun LockScreen(
    packageName: String,
    onClickClose: () -> Unit = {},
    onClickUnLock: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blackground),
    ) {
        val context = LocalContext.current
        val appName = context.getAppNameFromPackageName(packageName)

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 132.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = R.drawable.lock_on,
                contentDescription = "LockScreen Icon",
                modifier = Modifier.padding(bottom = 48.dp).size(120.dp),
            )
            Text(
                text = stringResource(R.string.target_usage_time_end),
                style = HmhTypography.headlineMedium,
                color = WhiteText,
            )
            Text(
                stringResource(R.string.use_it_anymore, appName),
                color = Gray2,
                style = TextStyle(textAlign = TextAlign.Center).merge(HmhTypography.bodyMedium),
                modifier = Modifier.padding(vertical = 10.dp),
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(R.string.remind_alarm_permission),
                style = TextStyle(textAlign = TextAlign.Center).merge(HmhTypography.bodySmall),
                color = Gray3,
                modifier = Modifier.padding(vertical = 21.dp).align(Alignment.CenterHorizontally),
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = BluePurpleButton),
                onClick = {
                    onClickClose()
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(vertical = 14.dp),
            ) {
                Text(
                    text = stringResource(R.string.close),
                    modifier = Modifier
                        .padding(horizontal = 70.dp, vertical = 10.dp),
                    style = HmhTypography.titleMedium,
                    color = WhiteBtn,
                )
            }
            Text(
                modifier = Modifier.clickable(onClick = onClickUnLock)
                    .padding(top = 22.dp, bottom = 58.dp),
                text = stringResource(R.string.do_unlock),
                style = HmhTypography.titleSmall,
                color = Gray1,
            )
        }
    }
}

fun killAppByPackageName(context: Context, packageName: String) {
    Log.d("LockActivity", "killAppByPackageName: $packageName")
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    try {
        activityManager?.killBackgroundProcesses(packageName)
    } catch (e: Exception) {
        context.toast(context.getString(R.string.app_kill_fail))
        Log.e("LockActivity", "killAppByPackageName error : $e")
    }
}
