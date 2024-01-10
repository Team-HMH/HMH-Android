@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.feature.main"
}

dependencies {

    // Navigation
    //Feature
    implementation(projects.feature.challenge)
    implementation(projects.feature.mypage)

    //Navigation
    implementation(libs.bundles.navigation)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    implementation(projects.core.viewmodel.main)
}
