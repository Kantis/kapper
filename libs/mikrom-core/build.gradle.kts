plugins {
   id("mikrom.conventions.lang.kotlin-multiplatform-js")
   id("mikrom.conventions.lang.kotlin-multiplatform-jvm")
   id("mikrom.conventions.lang.kotlin-multiplatform-native")
   id("mikrom.conventions.publishing.maven-publish")
}

kotlin {
   sourceSets {
      commonMain {
         dependencies {
            implementation(libs.jetbrains.annotations)
            implementation(libs.kotlinxCoroutinesCore)
         }
      }

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
            }
         }

         jvmTest {
            dependencies {
               implementation(libs.kotest.runnerJunit5)
            }
         }
      }
   }
}
