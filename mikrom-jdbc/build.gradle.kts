plugins {
   id("mikrom.conventions.lang.kotlin-multiplatform-jvm")
   id("mikrom.conventions.publishing.maven-publish")
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

      if (mikromSettings.enableKotlinJvm.get()) {
         jvmMain {
            dependencies {
               implementation(kotlin("reflect"))
               implementation(projects.mikromCore)
            }
         }

         jvmTest {
            dependencies {
               implementation(projects.mikromCompilerPlugin)
               implementation(libs.kotest.runnerJunit5)
               implementation(libs.h2)
            }
         }
      }
   }
}
