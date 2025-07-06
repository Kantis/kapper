plugins {
   `java-gradle-plugin`
   id("com.gradle.plugin-publish") version "1.1.0"
   id("mikrom.conventions.lang.kotlin-jvm")
   id("mikrom.conventions.api-validation")
}

group = "io.github.kantis"

dependencies {
   implementation(kotlin("gradle-plugin-api"))
}

gradlePlugin {
   website.set("https://github.com/kantis/mikrom")
   vcsUrl.set("https://github.com/kantis/mikrom.git")
   plugins {
      create("mikromGradlePlugin") {
         id = "io.github.kantis.mikrom.gradle-plugin"
         displayName = "Mikrom Plugin"
         description = "Mikrom Gradle plugin for Kotlin. Generates JDBC/R2DBC row- and parameter-mappers."
         implementationClass = "io.github.kantis.mikrom.gradle.MikromGradlePlugin"
         tags.set(listOf("kotlin", "mikrom"))
      }
   }
}
