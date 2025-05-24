plugins {
   id("kapper.conventions.lang.kotlin-multiplatform-js")
   id("kapper.conventions.lang.kotlin-multiplatform-jvm")
   id("kapper.conventions.lang.kotlin-multiplatform-native")
   id("kapper.conventions.publishing.maven-publish")
}

kotlin {
   sourceSets {
      commonMain {
         dependencies {
            implementation(libs.kotlinxSerialization.core)
            implementation(libs.kotlinxSerialization.json)
         }
      }

      commonTest {
         dependencies {
            implementation(libs.kotest.frameworkEngine)
            implementation(libs.kotest.assertionsCore)
            implementation(libs.kotest.assertionsJson)
            implementation(libs.kotest.property)

            implementation(libs.okio.core)

            implementation(libs.kotlinxSerialization.json)
         }
      }

      if (kapperSettings.enableKotlinJvm.get()) {
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
