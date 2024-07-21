@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.core.service"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)

    implementation(projects.domain.usagestats)
    implementation(projects.domain.lock)
    implementation(projects.domain.challenge)

    implementation(libs.lifecycle.process)
    implementation(libs.lifecycle.service)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.work.runtime.ktx)
}
