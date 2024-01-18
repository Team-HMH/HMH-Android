@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlin.coroutines)
    implementation(projects.core.domain)
}
