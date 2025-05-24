import dev.adamko.dokkatoo.dokka.plugins.DokkaHtmlPluginParameters

plugins {
   id("ks3.conventions.lang.kotlin-multiplatform-jvm")
   id("ks3.conventions.lang.kotlin-multiplatform-native")
   id("ks3.conventions.api-validation")
   idea
}

group = "io.ks3"

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
            "gradle/kotlin-js-store", // location of the lock file, overridden by Kotlin/JS convention
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
