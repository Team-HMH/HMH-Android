package com.hmh.hamyeonham.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

internal fun Project.configureAndroidCommonPlugin() {
    val properties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    apply<AndroidKotlinPlugin>()
    apply<KotlinSerializationPlugin>()
    with(plugins) {
        apply("kotlin-parcelize")
    }
    apply<AndroidHiltPlugin>()

    extensions.getByType<BaseExtension>().apply {
        defaultConfig {
            val kakaoApiKey = properties["kakaoApiKey"] as? String ?: ""
            manifestPlaceholders["kakaoApiKey"] = properties["kakaoApiKey"] as String
            buildConfigField("String", "KAKAO_API_KEY", "\"${kakaoApiKey}\"")

            val amplitudeApiKey = properties["amplitudeApiKey"] as? String ?: ""
            buildConfigField("String", "AMPLITUDE_API_KEY", "\"${amplitudeApiKey}\"")
        }
        buildTypes {
            getByName("debug") {
                buildConfigField("String", "HMH_BASE_URL", "\"${properties["hmhDevBaseUrl"]}\"")
            }
            getByName("release") {
                buildConfigField("String", "HMH_BASE_URL", "\"${properties["hmhProdBaseUrl"]}\"")
            }
        }
        buildFeatures.apply {
            viewBinding = true
            buildConfig = true
        }
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        "implementation"(libs.findLibrary("core.ktx").get())
        "implementation"(libs.findLibrary("appcompat").get())
        "implementation"(libs.findBundle("lifecycle").get())
        "implementation"(libs.findLibrary("material").get())
        "implementation"(libs.findLibrary("fragment.ktx").get())
        "implementation"(libs.findLibrary("timber").get())
        "implementation"(libs.findLibrary("amplitude").get())
    }
}
