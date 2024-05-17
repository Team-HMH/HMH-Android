package com.hmh.hamyeonham.plugin

import com.android.build.gradle.BaseExtension
import java.util.Properties
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureAndroidNetworkPlugin() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        "implementation"(libs.findBundle("retrofit").get())
    }
}
