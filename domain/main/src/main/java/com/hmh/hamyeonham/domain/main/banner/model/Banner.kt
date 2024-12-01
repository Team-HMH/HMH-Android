package com.hmh.hamyeonham.domain.main.banner.model

data class Banner(
    val title: String,
    val subTitle: String,
    val imageUrl: String,
    val linkUrl: String,
    val backgroundColors: List<String>
)
