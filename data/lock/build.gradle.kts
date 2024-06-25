@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.data.lock"
}

dependencies {

    implementation(projects.domain.lock)
    implementation(projects.domain.challenge)

    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.database)
}
