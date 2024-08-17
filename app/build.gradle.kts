import java.util.Properties

plugins {
    hmh("application")
    hmh("compose")
    hmh("test")
    alias(libs.plugins.google.services)
    alias(libs.plugins.app.distribution)
    alias(libs.plugins.crashlytics)
}

val properties =
    Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

android {
    namespace = "com.hmh.hamyeonham"

    defaultConfig {
        applicationId = "com.hmh.hamyeonham"
        versionCode =
            libs.versions.versionCode
                .get()
                .toInt()
        versionName = libs.versions.appVersion.get()
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = "android_debug_key"
            keyPassword = "android"
            storeFile = File("${project.rootDir.absolutePath}/keystore/debug.keystore")
            storePassword = "android"
        }
        create("release") {
            keyAlias = properties.getProperty("keyAlias")
            keyPassword = properties.getProperty("keyPassword")
            storeFile = File("${project.rootDir.absolutePath}/keystore/release.keystore.jks")
            storePassword = properties.getProperty("storePassword")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {

    // Feature
    implementation(projects.feature.login)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.main)
    implementation(projects.feature.mypage)
    implementation(projects.feature.challenge)
    implementation(projects.feature.lock)
    implementation(projects.feature.store)

    // Domain
    implementation(projects.domain.usagestats)
    implementation(projects.domain.userinfo)
    implementation(projects.domain.login)
    implementation(projects.domain.challenge)
    implementation(projects.domain.onboarding)
    implementation(projects.domain.point)
    implementation(projects.domain.lock)

    // Data
    implementation(projects.data.usagestats)
    implementation(projects.data.userinfo)
    implementation(projects.data.login)
    implementation(projects.data.challenge)
    implementation(projects.data.device)
    implementation(projects.data.onboarding)
    implementation(projects.data.point)
    implementation(projects.data.lock)

    // Core
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.viewmodel.main)
    implementation(projects.core.service)

    // Firebase
    implementation(platform(libs.firebase))
    implementation(libs.bundles.firebase)

    // Splash
    implementation(libs.splash.screen)
    implementation(libs.lottie)

    // kakao
    implementation(libs.kakao.login)

    // Hilt Worker
    implementation(libs.androidx.hilt.common)
}
