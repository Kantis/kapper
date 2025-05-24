plugins {
   id("kapper.conventions.lang.kotlin-multiplatform-jvm")
   id("kapper.conventions.lang.kotlin-multiplatform-native")
   id("kapper.conventions.api-validation")
   idea
}

group = "io.kapper"

kotlin {
   compilerOptions { freeCompilerArgs.add("-Xcontext-receivers") }

   sourceSets {
      commonTest {
         dependencies {
            implementation(libs.kotest.runnerJunit5)
            implementation(libs.kotest.assertionsCore)
            implementation(libs.kotest.frameworkApi)
         }
      }
   }
}

idea {
   module {
      isDownloadSources = true
      isDownloadJavadoc = false
      excludeDirs = excludeDirs +
         layout.files(
            ".idea",
            // location of the lock file, overridden by Kotlin/JS convention
            "gradle/kotlin-js-store",
            "gradle/wrapper",
         )

      // exclude generated Gradle code, so it doesn't clog up search results
      excludeDirs.addAll(
         layout.files(
            "build-logic/build/generated-sources/kotlin-dsl-accessors/kotlin/gradle",
            "build-logic/build/generated-sources/kotlin-dsl-external-plugin-spec-builders/kotlin/gradle",
            "build-logic/build/generated-sources/kotlin-dsl-plugins/kotlin",
            "build-logic/build/pluginUnderTestMetadata",
         ),
      )
   }
}
