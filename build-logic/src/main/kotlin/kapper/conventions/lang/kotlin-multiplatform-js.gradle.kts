package kapper.conventions.lang

import kapper.conventions.KapperBuildLogicSettings

plugins {
   id("kapper.conventions.lang.kotlin-multiplatform-base")
}

val kapperSettings = extensions.getByType<KapperBuildLogicSettings>()

if (kapperSettings.enableKotlinJs.get()) {
   kotlin {
      js(IR) {
         browser()
         nodejs()
      }
   }

   relocateKotlinJsStore()
}
