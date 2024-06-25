@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.data.login"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.domain.login)
    implementation(projects.core.network)
}
