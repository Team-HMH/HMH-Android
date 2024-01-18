package com.hmh.hamyeonham.feature.lock

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.feature.lock.ui.theme.Blackground
import com.hmh.hamyeonham.feature.lock.ui.theme.HMHAndroidTheme
import com.hmh.hamyeonham.feature.lock.ui.theme.HmhTypography
import com.hmh.hamyeonham.feature.lock.ui.theme.WhiteText

class LockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageName = intent.getStringExtra(PACKAGE_NAME).orEmpty()
        setContent {
            HMHAndroidTheme {
                LockScreen(packageName)
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
fun LockScreen(packageName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blackground)
    ) {
        val context = LocalContext.current
        val appName = context.getAppNameFromPackageName(packageName)
        val appIcon = context.packageManager.getApplicationIcon(packageName)


        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = appIcon,
                contentDescription = "App Image"
            )
            Spacer(modifier = Modifier.height(44.dp))
            Text(
                text = stringResource(R.string.target_usage_time_end),
                fontSize = 20.sp,
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFFDBDAE7),
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                stringResource(R.string.use_it_anymore, appName),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF8D8D9F),
                )
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D17D3)),
                onClick = {
                    killAppByPackageName(
                        context = context,
                        packageName = packageName
                    )
                },
            ) {
                Text(
                    text = stringResource(R.string.close),
                    modifier = Modifier.padding(horizontal = 70.dp, vertical = 10.dp),
                    style = HmhTypography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                stringResource(R.string.do_unlock),
                style = HmhTypography.titleSmall,
                color = WhiteText
            )
            Spacer(modifier = Modifier.height(38.dp))
        }
    }
}

fun killAppByPackageName(context: Context, packageName: String) {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    try {
        activityManager?.killBackgroundProcesses(packageName)
    } catch (e: Exception) {
        Log.e("LockActivity", "killAppByPackageName error : $e")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HMHAndroidTheme {
        LockScreen("HHM")
    }
}
