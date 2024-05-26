package com.hmh.hamyeonham.challenge.point

data class PointModel(
    val pointTitle: String = "",
    val pointWhatChallenge: String = "",
    val point: Int = 20,
    val getPointStatus: GetPointStatus = GetPointStatus.CAN_GET_POINT,
    val currentUserPoint: Int = 0,
) {
    enum class GetPointStatus {
        CAN_GET_POINT,
        ALREADY_GET_POINT,
        FAIL_CHALLENGE,
    }
}
