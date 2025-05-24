package kapper.conventions.lang

import kapper.conventions.KapperBuildLogicSettings
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.testing.KotlinTaskTestRun

plugins {
   id("kapper.conventions.base")
   kotlin("multiplatform")
   kotlin("plugin.serialization")
   id("io.kotest.multiplatform")
   id("org.jlleitschuh.gradle.ktlint")
}

// Base configuration for all Kotlin/Multiplatform conventions.
// This plugin does not enable any Kotlin target. To enable a target in a subproject, prefer
// applying specific Kotlin target convention plugins.

val kapperSettings = extensions.getByType<KapperBuildLogicSettings>()

kotlin {
   jvmToolchain {
      languageVersion.set(JavaLanguageVersion.of(kapperSettings.jvmTarget.get()))
   }

   sourceSets {
      all {
         languageSettings.optIn("io.kapper.core.kapperInternal")
         languageSettings.optIn("kotlin.RequiresOptIn")
      }
   }

   targets.configureEach {
      compilations.configureEach {
         kotlinOptions {
            freeCompilerArgs += "-Xcontext-receivers"
            apiVersion = kapperSettings.kotlinTarget.get()
            languageVersion = kapperSettings.kotlinTarget.get()
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
         languageVersion = kapperSettings.kotlinTarget.get()
         apiVersion = kapperSettings.kotlinTarget.get()
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
