plugins {
   id("mikrom.conventions.lang.kotlin-multiplatform-jvm")
   id("mikrom.conventions.publishing.maven-publish")
}

kotlin {
   sourceSets {
      if (mikromSettings.enableKotlinJvm.get()) {
         jvmMain {
            dependencies {
               compileOnly(kotlin("compiler"))
               implementation(projects.mikromCore)
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
