package kapper.conventions

plugins {
   id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

apiValidation {
   @OptIn(kotlinx.validation.ExperimentalBCVApi::class)
   klib {
      enabled = System.getProperty("enableKlibValidation") != null
   }
}
