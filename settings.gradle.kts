rootProject.name = "mikrom"

apply(from = "build-logic/repositories.gradle.kts")

includeBuild("build-logic")

include(
   ":mikrom-core",
   ":mikrom-compiler-plugin",
   ":mikrom-jdbc",
   ":compiler-plugin-test",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
