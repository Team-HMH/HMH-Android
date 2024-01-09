@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.hmh.hamyeonham.feature.statistics"
}

dependencies {

    // Domain
    implementation(projects.domain.usagestats)

    // Core
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
}
