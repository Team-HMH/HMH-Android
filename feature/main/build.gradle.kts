@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.feature.main"
}

dependencies {

    // Feature
    implementation(projects.feature.challenge)
    implementation(projects.feature.mypage)

    // Domain
    implementation(projects.domain.lock)
    implementation(projects.domain.usagestats)
    implementation(projects.domain.userinfo)

    // Core
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.viewmodel.main)
    implementation(projects.core.service)
    implementation(projects.core.network)

    // third party
    implementation(libs.bundles.navigation)
    implementation(libs.lottie)
    implementation(libs.coil.core)
}
