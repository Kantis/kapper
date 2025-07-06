rootProject.name = "mikrom"

apply(from = "../build-logic/repositories.gradle.kts")

includeBuild("../build-logic")

include(
   ":mikrom-core",
   ":mikrom-jdbc",
   ":mikrom-r2dbc",
   ":example",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

dependencyResolutionManagement {
   versionCatalogs {
      create("libs") {
         from(files("../gradle/libs.versions.toml"))
      }
   }
}
