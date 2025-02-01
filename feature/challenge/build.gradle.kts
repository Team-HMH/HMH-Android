@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
    hmh("compose")
}

android {
    namespace = "com.hmh.hamyeonham.feature.challenge"
}

dependencies {
    implementation(projects.domain.usagestats)
    implementation(projects.domain.challenge)
    implementation(projects.domain.point)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.viewmodel.main)
    implementation(projects.core.domain)

    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.work.runtime.ktx)
}
