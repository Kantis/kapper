plugins {
   id("mikrom.conventions.lang.kotlin-multiplatform-jvm")
   id("io.github.kantis.mikrom")
}

kotlin {
   sourceSets {
      jvmMain {
         dependencies {
            implementation(libs.h2)
            implementation(libs.mikrom.core)
            implementation(libs.mikrom.jdbc)
         }
      }
   }
}

tasks.named("compileKotlinJvm").configure {
   outputs.upToDateWhen { false }
}
