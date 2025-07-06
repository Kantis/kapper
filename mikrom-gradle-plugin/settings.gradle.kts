includeBuild("../mikrom-compiler-plugin")
includeBuild("../build-logic")

apply(from = "../build-logic/repositories.gradle.kts")

dependencyResolutionManagement {
   versionCatalogs {
      create("libs") {
         from(files("../gradle/libs.versions.toml"))
      }
   }
}
