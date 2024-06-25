@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.data.userinfo"
}

dependencies {
    implementation(projects.domain.userinfo)
    implementation(projects.core.network)
    implementation(projects.core.common)
}
