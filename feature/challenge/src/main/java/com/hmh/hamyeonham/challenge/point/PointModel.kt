package com.hmh.hamyeonham.challenge.point

data class PointModel(
    val pointTitle: String,
    val pointWhatChallenge: String,
    val point: Int,
    val getPointStatus: GetPointStatus,
) {
    enum class GetPointStatus {
        CAN_GET_POINT,
        ALREADY_GET_POINT,
        FAIL_CHALLENGE,
    }
}
