plugins {
   kotlin("multiplatform") version libs.versions.kotlin.get()
   id("io.github.kantis.mikrom.gradle-plugin")
}

kotlin {
   compilerOptions { freeCompilerArgs.add("-Xcontext-receivers") }
   jvm()

   sourceSets {
      jvmMain {
         dependencies {
            implementation(libs.h2)
            implementation("io.github.kantis.mikrom:mikrom-core:0.1.0-SNAPSHOT")
            implementation("io.github.kantis.mikrom:mikrom-jdbc:0.1.0-SNAPSHOT")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
         }
      }
   }
}

tasks.named("compileKotlinJvm").configure {
   outputs.upToDateWhen { false }
}
