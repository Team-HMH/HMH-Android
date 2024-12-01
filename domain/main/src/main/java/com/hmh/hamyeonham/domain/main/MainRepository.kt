package com.hmh.hamyeonham.domain.main

import com.hmh.hamyeonham.domain.main.banner.model.Banner

interface MainRepository {
    suspend fun getBanner(): Banner
}