package com.hmh.hamyeonham.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureAndroidCommonPlugin() {

    apply<AndroidKotlinPlugin>()
    apply<KotlinSerializationPlugin>()
    with(plugins) {
        apply("kotlin-parcelize")
    }
    apply<AndroidHiltPlugin>()

    extensions.getByType<BaseExtension>().apply {
        defaultConfig {
            val kakaoApiKey = properties["kakaoApiKey"] as? String ?: ""

            manifestPlaceholders["kakaoApiKey"] = properties["kakaoApiKey"] as? String ?: ""

            buildConfigField("String", "KAKAO_API_KEY", "\"${kakaoApiKey}\"")
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
    }
}
