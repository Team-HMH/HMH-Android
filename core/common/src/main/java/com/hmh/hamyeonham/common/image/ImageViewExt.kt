package com.hmh.hamyeonham.common.image

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import timber.log.Timber

fun ImageView.setBackgroundColors(backgroundColors: List<String>): ImageView {
    try {
        val gradientDrawable = GradientDrawable()

        when (backgroundColors.size) {
            // 단일 색상
            1 -> {
                gradientDrawable.setColor(Color.parseColor(backgroundColors[0]))
            }
            // 그라데이션 (두 가지 색상 이상)
            else -> {
                gradientDrawable.colors = backgroundColors.map { Color.parseColor(it) }.toIntArray()
                gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
                gradientDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
            }
        }

        this.background = gradientDrawable
    } catch (e: Exception) {
        Timber.e(e)
    }

    return this
}

fun ImageView.setCornerRadius(radius: Float): ImageView {
    val gradientDrawable = (this.background as? GradientDrawable) ?: GradientDrawable()

    gradientDrawable.cornerRadius = radius
    this.background = gradientDrawable
    return this
}
