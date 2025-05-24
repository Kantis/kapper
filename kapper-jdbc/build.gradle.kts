plugins {
   id("kapper.conventions.lang.kotlin-multiplatform-jvm")
   id("kapper.conventions.publishing.maven-publish")
}

kotlin {
   sourceSets {
      commonTest {
         dependencies {
            implementation(libs.kotest.frameworkEngine)
            implementation(libs.kotest.assertionsCore)
            implementation(libs.kotest.assertionsJson)
            implementation(libs.kotest.property)
         }
      }

      if (kapperSettings.enableKotlinJvm.get()) {
         jvmMain {
            dependencies {
               implementation(kotlin("reflect"))
               implementation(projects.kapperCore)
            }
         }

         jvmTest {
            dependencies {
               implementation(libs.kotest.runnerJunit5)
               implementation(libs.h2)
            }
         }
      }
   }
}
