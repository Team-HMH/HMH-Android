@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
    hmh("compose")
}

android {
    namespace = "com.hmh.hamyeonham.feature.lock"
}

dependencies {

    implementation(projects.domain.usagestats)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    implementation(libs.lifecycle.process)
}
