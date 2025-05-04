package com.hmh.hamyeonham.login.model

data class User(
    val id: Long?,
)


// TODO 필요한 User 데이터 추가 -> 과연 도메인 상 유저에 어떤 값이 필요한지...?
//data class User(
//    val id: Long?,
//    val properties: Map<String, String>?,
//    val kakaoAccount: Account?,
//    val groupUserToken: String?,
//    val connectedAt: Date?,
//    val synchedAt: Date?,
//    val hasSignedUp: Boolean?,
//    val uuid: String?,
//)