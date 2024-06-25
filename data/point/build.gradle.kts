@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.data.point"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.common)

    implementation(projects.domain.point)
}