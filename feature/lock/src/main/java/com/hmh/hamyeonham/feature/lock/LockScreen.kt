package com.hmh.hamyeonham.feature.lock

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
import com.hmh.hamyeonham.common.amplitude.AmplitudeUtils
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.core.designsystem.compose.theme.Blackground
import com.hmh.hamyeonham.core.designsystem.compose.theme.BluePurpleButton
import com.hmh.hamyeonham.core.designsystem.compose.theme.Gray1
import com.hmh.hamyeonham.core.designsystem.compose.theme.Gray2
import com.hmh.hamyeonham.core.designsystem.compose.theme.Gray3
import com.hmh.hamyeonham.core.designsystem.compose.theme.HmhTypography
import com.hmh.hamyeonham.core.designsystem.compose.theme.WhiteBtn
import com.hmh.hamyeonham.core.designsystem.compose.theme.WhiteText

@Composable
fun LockScreen(
    packageName: String,
    onClickClose: () -> Unit = {},
    onClickUnLock: () -> Unit = {},
) {
    AmplitudeUtils.trackEventWithProperties("view_unlock_popup")
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
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .size(120.dp),
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
                modifier = Modifier
                    .padding(vertical = 21.dp)
                    .align(Alignment.CenterHorizontally),
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
                modifier = Modifier
                    .clickable(onClick = {
                        onClickUnLock()
                        AmplitudeUtils.trackEventWithProperties("click_unlock_button")
                    })
                    .padding(top = 22.dp, bottom = 58.dp),
                text = stringResource(R.string.do_unlock),
                style = HmhTypography.titleSmall,
                color = Gray1,
            )
        }
    }
}
