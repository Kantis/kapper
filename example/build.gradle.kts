plugins {
   id("mikrom.conventions.lang.kotlin-multiplatform-jvm")
   id("io.github.kantis.mikrom.gradle-plugin")
}

kotlin {
   compilerOptions { freeCompilerArgs.add("-Xcontext-receivers") }
   jvmToolchain {
      languageVersion.set(JavaLanguageVersion.of(21))
   }

   sourceSets {
      jvmMain {
         dependencies {
            implementation(libs.h2)
            implementation("io.github.kantis:mikrom-core:0.1.0-SNAPSHOT")
            implementation("io.github.kantis:mikrom-jdbc:0.1.0-SNAPSHOT")
         }
      }
   }
}

tasks.named("compileKotlinJvm").configure {
   outputs.upToDateWhen { false }
}
