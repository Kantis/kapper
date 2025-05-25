package mikrom.conventions.lang

import mikrom.conventions.MikromBuildLogicSettings
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.testing.KotlinTaskTestRun

plugins {
   id("mikrom.conventions.base")
   kotlin("multiplatform")
   id("io.kotest.multiplatform")
   id("org.jlleitschuh.gradle.ktlint")
}

// Base configuration for all Kotlin/Multiplatform conventions.
// This plugin does not enable any Kotlin target. To enable a target in a subproject, prefer
// applying specific Kotlin target convention plugins.

val mikromSettings = extensions.getByType<MikromBuildLogicSettings>()

kotlin {
   explicitApi()
   jvmToolchain {
      languageVersion.set(JavaLanguageVersion.of(mikromSettings.jvmTarget.get()))
   }

   sourceSets {
      all {
         languageSettings.optIn("com.github.kantis.mikrom.MikromInternal")
         languageSettings.optIn("kotlin.RequiresOptIn")
      }
   }

   targets.configureEach {
      compilations.configureEach {
         kotlinOptions {
            freeCompilerArgs += "-Xcontext-receivers"
            apiVersion = mikromSettings.kotlinTarget.get()
            languageVersion = mikromSettings.kotlinTarget.get()
         }
      }
   }

   // configure all Kotlin/JVM Tests to use JUnit
   targets.withType<KotlinJvmTarget>().configureEach {
      testRuns.configureEach {
         executionTask.configure {
            useJUnitPlatform()
         }
      }
   }

   sourceSets.configureEach {
      languageSettings {
         languageVersion = mikromSettings.kotlinTarget.get()
         apiVersion = mikromSettings.kotlinTarget.get()
         optIn("kotlin.RequiresOptIn")
      }
   }
}

// create lifecycle task for each Kotlin Platform, that will run all tests
KotlinPlatformType.values().forEach { kotlinPlatform ->
   val kotlinPlatformName = kotlinPlatform.name.capitalized()

   val testKotlinTargetLifecycleTask =
      tasks.create("allKotlin${kotlinPlatformName}Tests") {
         group = LifecycleBasePlugin.VERIFICATION_GROUP
         description = "Run all Kotlin/$kotlinPlatformName tests"
      }

   kotlin.testableTargets.matching {
      it.platformType == kotlinPlatform
   }.configureEach {
      testRuns.configureEach {
         if (this is KotlinTaskTestRun<*, *>) {
            testKotlinTargetLifecycleTask.dependsOn(executionTask)
         }
      }
   }
}
