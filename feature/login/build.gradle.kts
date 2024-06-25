@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.feature.login"
}

dependencies {

    // Domain
    implementation(projects.domain.login)

    // Common
    implementation(projects.core.common)

    // kakao
    implementation(libs.kakao.login)

    // coil
    implementation(libs.coil.core)

    // dot indicator
    implementation(libs.dot.indicator)

    implementation(projects.core.designsystem)
    implementation(projects.core.network)
    implementation(projects.core.database)

    implementation(projects.feature.onboarding)
}
