plugins {
    `kotlin-dsl`
}

group = "com.hmh.hamyeonham.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


dependencies {
    compileOnly(libs.agp)
    compileOnly(libs.kotlin.gradleplugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.compiler.extension)
}

gradlePlugin {
    plugins {
        create("android-application") {
            id = "com.hmh.hamyeonham.application"
            implementationClass = "com.hmh.hamyeonham.plugin.AndroidApplicationPlugin"
        }
        create("android-feature") {
            id = "com.hmh.hamyeonham.feature"
            implementationClass = "com.hmh.hamyeonham.plugin.AndroidFeaturePlugin"
        }
        create("android-data") {
            id = "com.hmh.hamyeonham.data"
            implementationClass = "com.hmh.hamyeonham.plugin.AndroidDataPlugin"
        }
        create("android-kotlin") {
            id = "com.hmh.hamyeonham.kotlin"
            implementationClass = "com.hmh.hamyeonham.plugin.AndroidKotlinPlugin"
        }
        create("android-hilt") {
            id = "com.hmh.hamyeonham.hilt"
            implementationClass = "com.hmh.hamyeonham.plugin.AndroidHiltPlugin"
        }
        create("android-room") {
            id = "com.hmh.hamyeonham.room"
            implementationClass = "com.hmh.hamyeonham.plugin.AndroidRoomConventionPlugin"
        }
        create("kotlin-serialization") {
            id = "com.hmh.hamyeonham.serialization"
            implementationClass = "com.hmh.hamyeonham.plugin.KotlinSerializationPlugin"
        }
        create("junit5") {
            id = "com.hmh.hamyeonham.junit5"
            implementationClass = "com.hmh.hamyeonham.plugin.JUnit5Plugin"
        }
        create("android-test") {
            id = "com.hmh.hamyeonham.test"
            implementationClass = "com.hmh.hamyeonham.plugin.AndroidTestPlugin"
        }
        create("compose") {
            id = "com.hmh.hamyeonham.compose"
            implementationClass = "com.hmh.hamyeonham.plugin.ComposePlugin"
        }
    }
}

