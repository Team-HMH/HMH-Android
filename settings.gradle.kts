enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "HMH-Android"
include(":app")
include(":core:common")
include(":core:database")
include(":feature:onboarding")
include(":feature:main")
include(":core:designsystem")

include(":feature:statistics")
include(":feature:login")

include(":data:usagestats")
include(":domain:usagestats")
include(":data:onboarding")
