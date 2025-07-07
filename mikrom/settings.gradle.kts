rootProject.name = "mikrom"

includeBuild("../build-logic")
apply(from = "../build-logic/repositories.gradle.kts")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(
   ":mikrom-core",
   ":mikrom-jdbc",
   ":mikrom-r2dbc",
)

dependencyResolutionManagement {
   versionCatalogs {
      create("libs") {
         from(files("../gradle/libs.versions.toml"))
      }
   }
}
