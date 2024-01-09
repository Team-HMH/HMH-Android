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

include(":feature:statistics")
include(":feature:login")
include(":feature:challenge")
include(":feature:onboarding")
include(":feature:main")

include(":data:usagestats")
include(":data:onboarding")

include(":domain:usagestats")
include(":domain:challenge")

include(":core:common")
include(":core:database")
include(":core:designsystem")
include(":core:viewmodel:main")


