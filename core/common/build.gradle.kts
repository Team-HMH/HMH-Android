@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    hmh("feature")
    hmh("compose")
    hmh("test")
}

android {
    namespace = "com.hmh.hamyeonham.common"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.retrofit)
}
