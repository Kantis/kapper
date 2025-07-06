rootProject.name = "mikrom"

apply(from = "build-logic/repositories.gradle.kts")

includeBuild("build-logic")

include(
   ":mikrom-core",
   ":mikrom-jdbc",
   ":mikrom-r2dbc",
   ":example",
)

includeBuild("mikrom-gradle-plugin")
includeBuild("mikrom-compiler-plugin")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
