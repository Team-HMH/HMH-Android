@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.feature.store"
}

dependencies {
    implementation(libs.fragment.ktx)
    implementation(libs.retrofit)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)

}
