package com.hmh.hamyeonham.core.designsystem.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val Blackground = Color(0xFF17171B)
val Gray8 = Color(0xFF1B1B23)
val Gray7 = Color(0xFF202028)
val Gray6 = Color(0xFF272934)
val Gray5 = Color(0xFF363948)
val Gray4 = Color(0xFF3D3F4D)
val Gray3 = Color(0xFF50505E)
val Gray2 = Color(0xFF8D8D9F)
val Gray1 = Color(0xFFA5A5BB)
val WhiteText = Color(0xFFDBDAE7)

val BluePurpleButton = Color(0xFF3D17D3)
val BluePurpleLine = Color(0xFF461EE7)
val BluePurpleProgress = Color(0xFF4428EC)
val BluePurpleText = Color(0xFF3726F8)

val BluePurpleOpacity70 = Color(0xB33D17D3) // 70% opacity
val BluePurpleOpacity22 = Color(0x382B1DD6) // 22% opacity

val WhiteBtn = Color(0xFFEBECF4)

@Immutable
data class HMHColor(
    val blackground: Color,
    val gray8: Color,
    val gray7: Color,
    val gray6: Color,
    val gray5: Color,
    val gray4: Color,
    val gray3: Color,
    val gray2: Color,
    val gray1: Color,
    val whiteText: Color,
    val bluePurpleButton: Color,
    val bluePurpleLine: Color,
    val bluePurpleProgress: Color,
    val bluePurpleText: Color,
    val bluePurpleOpacity70: Color,
    val bluePurpleOpacity22: Color,
    val whiteBtn: Color,
)

val hmhColors = HMHColor(
    blackground = Blackground,
    gray8 = Gray8,
    gray7 = Gray7,
    gray6 = Gray6,
    gray5 = Gray5,
    gray4 = Gray4,
    gray3 = Gray3,
    gray2 = Gray2,
    gray1 = Gray1,
    whiteText = WhiteText,
    bluePurpleButton = BluePurpleButton,
    bluePurpleLine = BluePurpleLine,
    bluePurpleProgress = BluePurpleProgress,
    bluePurpleText = BluePurpleText,
    bluePurpleOpacity70 = BluePurpleOpacity70,
    bluePurpleOpacity22 = BluePurpleOpacity22,
    whiteBtn = WhiteBtn,
)