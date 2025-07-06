includeBuild("../libs")
apply(from = "../build-logic/repositories.gradle.kts")

includeBuild("../build-logic")
dependencyResolutionManagement {
   versionCatalogs {
      create("libs") {
         from(files("../gradle/libs.versions.toml"))
      }
   }
}
