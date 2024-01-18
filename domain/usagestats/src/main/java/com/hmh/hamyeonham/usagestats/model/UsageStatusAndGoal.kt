package com.hmh.hamyeonham.usagestats.model

data class UsageStatusAndGoal(
    val packageName: String,
    val totalTimeInForeground: Long,
    val goalTime: Long,
) {
    private val challengeSuccess: Boolean = (goalTime - totalTimeInForeground) >= 0
    val timeLeft: Long by lazy {
        if (challengeSuccess) goalTime - totalTimeInForeground else 0L
    }
    val usedPercentage: Int by lazy {
        if (challengeSuccess) (totalTimeInForeground * 100 / goalTime).toInt() else 100
    }
    private val level = usedPercentage / 25 + 1
    val blackhole: Blackhole? = Blackhole.createByLevel(level)

    fun getBlackholeUri(): String = blackhole?.getVideoUri() ?: "no blackhole exist"
    fun getBlackholeDescription(): String = blackhole?.description ?: "no blackhole exist"
}

enum class Blackhole(val level: Int, val description: String, val fileName: String) {
    FIRST(1, "님을 위한\n우주가 생성되었어요", "vd_blackhole1"),
    SECOND(2, "블랙홀이 다가와요\n도파민의 유혹을 이겨내요", "vd_blackhole2"),
    THIRD(3, "블랙홀이 가까워졌어요\n스마트폰을 멀리해볼까요?", "vd_blackhole3"),
    FOURTH(4, "블랙홀에 빠질 수 있어요\n스마트폰을 내려놓아요", "vd_blackhole4"),
    FIFTH(5, "지금부터 앱을 사용하면\n챌린지를 실패해요", "vd_blackhole5"),
    ;

    fun getVideoUri(): String {
        return baseVideoUri + fileName
    }

    companion object {
        val baseVideoUri = "android.resource://com.hmh.hamyeonham/raw/"

        fun createByLevel(level: Int): Blackhole? {
            return entries.find { it.level == level }
        }
    }
}
