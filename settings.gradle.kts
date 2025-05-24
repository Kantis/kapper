rootProject.name = "kapper"

apply(from = "build-logic/repositories.gradle.kts")

includeBuild("build-logic")

include(
   ":kapper-core",
   ":kapper-jdbc",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
