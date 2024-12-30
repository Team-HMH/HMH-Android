package com.hmh.hamyeonham.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidDataPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        plugins.apply("com.android.library")
        configureAndroidCommonPlugin()
        configureAndroidNetworkPlugin()
    }
}
