@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    hmh("feature")
}

android {
    namespace = "com.hmh.hamyeonham.data.usagestats"
}

dependencies {
    // Domain
    implementation(projects.domain.usagestats)
    // Core
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(projects.core.domain)
    implementation(project(":core:common"))
}
