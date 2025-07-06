includeBuild("../build-logic")
includeBuild("../libs")
includeBuild("../mikrom-compiler-plugin")
includeBuild("../mikrom-gradle-plugin")

apply(from = "../build-logic/repositories.gradle.kts")

dependencyResolutionManagement {
   versionCatalogs {
      create("libs") {
         from(files("../gradle/libs.versions.toml"))
      }
   }
}
