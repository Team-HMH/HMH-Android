package com.hmh.hamyeonham.data.main

import com.hmh.hamyeonham.core.network.call.util.getResult
import com.hmh.hamyeonham.core.network.main.MainService
import com.hmh.hamyeonham.domain.main.MainRepository
import com.hmh.hamyeonham.domain.main.banner.model.Banner
import com.hmh.hamyeonham.login.mapper.toBanner
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val mainService: MainService
) : MainRepository {
    override suspend fun getBanner(): Result<Banner> {
        return mainService.getBanner().getResult().mapCatching { response ->
                response.toBanner()
        }
    }
}