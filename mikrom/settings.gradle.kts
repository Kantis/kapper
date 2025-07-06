includeBuild("../build-logic")

apply(from = "../build-logic/repositories.gradle.kts")

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
