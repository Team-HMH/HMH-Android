package com.hmh.hamyeonham.core.designsystem.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hmh.hamyeonham.core.designsystem.R

val PretendardMedium = FontFamily(
    Font(
        R.font.pretendard_medium,
        FontWeight.Medium
    )
)
val PretendardRegular = FontFamily(
    Font(
        R.font.pretendard_regular,
        FontWeight.Normal
    )
)
val PretendardSemiBold = FontFamily(
    Font(
        R.font.pretendard_semibold,
        FontWeight.SemiBold
    )
)
val HmhTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = PretendardRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 31.sp,
        lineHeight = 46.5.sp // 1.5 line spacing
    ),
    displayMedium = TextStyle(
        fontFamily = PretendardSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 23.sp,
        lineHeight = 34.5.sp // 1.5 line spacing
    ),
    displaySmall = TextStyle(
        fontFamily = PretendardSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 21.sp,
        lineHeight = 31.5.sp // 1.5 line spacing
    ),
    headlineLarge = TextStyle(
        fontFamily = PretendardMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 21.sp,
        lineHeight = 31.5.sp // 1.5 line spacing
    ),
    headlineMedium = TextStyle(
        fontFamily = PretendardSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 28.5.sp // 1.5 line spacing
    ),
    headlineSmall = TextStyle(
        fontFamily = PretendardMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp,
        lineHeight = 26.6.sp // 1.4 line spacing
    ),
    titleLarge = TextStyle(
        fontFamily = PretendardSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 25.5.sp // 1.5 line spacing
    ),
    titleMedium = TextStyle(
        fontFamily = PretendardSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.5.sp // 1.5 line spacing
    ),
    titleSmall = TextStyle(
        fontFamily = PretendardMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.5.sp // 1.5 line spacing
    ),
    bodyLarge = TextStyle(
        fontFamily = PretendardMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PretendardRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 19.5.sp // 1.5 line spacing
    ),
    bodySmall = TextStyle(
        fontFamily = PretendardSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.8.sp // 1.4 line spacing
    ),
    labelLarge = TextStyle(
        fontFamily = PretendardSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 15.4.sp // 1.4 line spacing
    ),
    labelMedium = TextStyle(
        fontFamily = PretendardMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.4.sp // 1.4 line spacing
    )
)

