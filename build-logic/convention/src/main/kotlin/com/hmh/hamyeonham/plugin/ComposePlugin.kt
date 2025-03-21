package com.hmh.hamyeonham.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply("org.jetbrains.kotlin.plugin.compose")

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
            featureFlags.set(setOf(ComposeFeatureFlag.StrongSkipping))
            includeSourceInformation.set(true)
        }

        dependencies {
            "implementation"(platform(libs.findLibrary("compose.bom").get()))
            "implementation"(libs.findBundle("compose").get())
        }
    }
}
