includeBuild("../build-logic")
includeBuild("../mikrom")

apply(from = "../build-logic/repositories.gradle.kts")

dependencyResolutionManagement {
   versionCatalogs {
      create("libs") {
         from(files("../gradle/libs.versions.toml"))
      }
   }
}
