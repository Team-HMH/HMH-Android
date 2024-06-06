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

    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.domain)

    implementation(libs.retrofit)

    // TEST
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("io.mockk:mockk:1.12.1")
}
