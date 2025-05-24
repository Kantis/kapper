rootProject.name = "kapper"

apply(from = "build-logic/repositories.gradle.kts")

includeBuild("build-logic")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
