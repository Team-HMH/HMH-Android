package com.hmh.hamyeonham.challenge.repository

import com.hmh.hamyeonham.challenge.model.Apps

interface ChallengeRepository {
    suspend fun postApps(request: Apps): Result<Unit>
    suspend fun deleteApps(appCode: String): Result<Unit>
}
