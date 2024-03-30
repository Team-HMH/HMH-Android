package com.hmh.hamyeonham.challenge.model

data class Apps(
    val apps: List<App>
) {
    data class App(
        val appCode: String,
        val goalTime: Long
    )

    companion object {
        fun createFromAppCodeListAndGoalTime(appCodeList: List<String>, goalTime: Long): Apps {
            val appList = appCodeList.map { appCode ->
                App(appCode, goalTime)
            }
            return Apps(apps = appList)
        }
    }
}
