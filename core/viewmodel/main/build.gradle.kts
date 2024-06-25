@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.core.main"
}

dependencies {
    implementation(projects.domain.usagestats)
    implementation(projects.domain.challenge)
    implementation(projects.domain.userinfo)
    implementation(projects.domain.point)
    implementation(projects.domain.lock)

    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.domain)

    implementation(libs.retrofit)
}
