rootProject.name = "mikrom"

apply(from = "build-logic/repositories.gradle.kts")

includeBuild("build-logic")

include(
   ":mikrom-core",
   ":mikrom-compiler-plugin",
   ":mikrom-compiler-plugin-test",
   ":mikrom-jdbc",
   ":example",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
