plugins {
   base
   `java-gradle-plugin`
   id("com.gradle.plugin-publish") version "1.1.0"
   kotlin("jvm") version libs.versions.kotlin.get()
}

group = "io.github.kantis.mikrom"

dependencies {
   implementation(kotlin("stdlib"))
   implementation(kotlin("gradle-plugin-api"))
}

gradlePlugin {
   website.set("https://github.com/kantis/mikrom")
   vcsUrl.set("https://github.com/kantis/mikrom.git")
   plugins {
      create("mikromGradlePlugin") {
         id = "io.github.kantis.mikrom.gradle-plugin"
         displayName = "Kotlin Power Assertion Plugin"
         description = "Mikrom Gradle Plugin "
         implementationClass = "io.github.kantis.mikrom.gradle.MikromGradlePlugin"
         tags.set(listOf("kotlin", "mikrom"))
      }
   }
}
