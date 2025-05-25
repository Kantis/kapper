package mikrom.conventions.lang

import mikrom.conventions.MikromBuildLogicSettings

plugins {
   id("mikrom.conventions.lang.kotlin-multiplatform-base")
}

val mikromSettings = extensions.getByType<MikromBuildLogicSettings>()

if (mikromSettings.enableKotlinJvm.get()) {
   kotlin {
      jvm {
         withJava()
      }
   }
}
