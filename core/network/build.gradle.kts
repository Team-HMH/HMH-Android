@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
    hmh("serialization")
}

android {
    namespace = "com.hmh.hamyeonham.core.network"
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.security)
    implementation(libs.bundles.retrofit)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.process.phoenix)
    implementation(projects.domain.login)
    implementation(libs.kakao.login)

    implementation(projects.core.database)
}
