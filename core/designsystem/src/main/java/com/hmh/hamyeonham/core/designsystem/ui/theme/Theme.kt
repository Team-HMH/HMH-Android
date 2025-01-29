package com.hmh.hamyeonham.core.designsystem.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Gray7,
    onPrimary = WhiteBtn,
    primaryContainer = Gray8,
    secondary = Gray3,
    onSecondary = WhiteText,
    secondaryContainer = Gray4,
    onSecondaryContainer = Gray2,
    background = Blackground,
    onBackground = WhiteText
)


private val LightColorScheme = lightColorScheme(
    primary = Gray7,
    onPrimary = WhiteBtn,
    primaryContainer = Gray8,
    secondary = Gray3,
    onSecondary = WhiteText,
    secondaryContainer = Gray4,
    onSecondaryContainer = Gray2,
    background = Blackground,
    onBackground = WhiteText
)

@Composable
fun HMHAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    if (!LocalInspectionMode.current) {
        val view = LocalView.current
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HmhTypography,
        content = content
    )
}
