package com.hmh.hamyeonham.common.image

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import timber.log.Timber

fun ImageView.setBackgroundColors(backgroundColors: List<String>) {
    try {
        val gradientDrawable = GradientDrawable()

        when (backgroundColors.size) {
            // 단일 색상
            1 -> {
                gradientDrawable.setColor(Color.parseColor(backgroundColors[0]))
            }
            // 그라데이션 (두 가지 색상)
            2 -> {
                gradientDrawable.colors = backgroundColors.map { Color.parseColor(it) }.toIntArray()
                gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            }
            // 세 가지 이상의 색상 그라데이션
            else -> {
                gradientDrawable.colors = backgroundColors.map { Color.parseColor(it) }.toIntArray()
                gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            }
        }

        this.background = gradientDrawable
    } catch (e: Exception) {
        Timber.e(e)
    }
}