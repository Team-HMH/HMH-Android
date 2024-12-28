package com.hmh.hamyeonham.login.mapper

import com.hmh.hamyeonham.core.network.main.model.BannerResponse
import com.hmh.hamyeonham.domain.main.banner.model.Banner

internal fun BannerResponse.toBanner(): Banner {
    return Banner(
        title = this.title.orEmpty(),
        subTitle = this.subTitle.orEmpty(),
        imageUrl = this.imageUrl.orEmpty(),
        linkUrl = this.linkUrl.orEmpty(),
        backgroundColors = this.backgroundColors.orEmpty()
    )
}



