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

    // Navigation
    implementation(libs.bundles.navigation)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.viewmodel.main)

    // domain
    implementation(projects.domain.usagestats)
}
