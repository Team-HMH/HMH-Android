@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    hmh("feature")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.hmh.hamyeonham.feature.store"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.viewmodel.main)
    implementation(projects.core.service)
    implementation(projects.core.network)
}