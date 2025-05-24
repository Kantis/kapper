package kapper.conventions

import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import javax.inject.Inject

/**
 * Common settings for configuring kapper's build logic.
 *
 * The settings need to be accessible during configuration, so they should come from Gradle
 * properties or environment variables.
 */
abstract class KapperBuildLogicSettings @Inject constructor(
   private val providers: ProviderFactory,
) {

   val kotlinTarget: Provider<String> = kapperSetting("kotlinTarget", "1.8")
   val jvmTarget: Provider<String> = kapperSetting("jvmTarget", "11")

   /** Controls whether Kotlin Multiplatform JVM is enabled */
   val enableKotlinJvm: Provider<Boolean> = kapperFlag("enableKotlinJvm", true)
   /** Controls whether Kotlin Multiplatform JS is enabled */
   val enableKotlinJs: Provider<Boolean> = kapperFlag("enableKotlinJs", true)
   /** Controls whether Kotlin Multiplatform Native is enabled */
   val enableKotlinNative: Provider<Boolean> = kapperFlag("enableKotlinNative", false)

   /**
    * Comma separated list of MavenPublication names that will have the publishing task enabled.
    * The provided names will be matched ignoring case, and by prefix, so `iOS` will match
    * `iosArm64`, `iosX64`, and `iosSimulatorArm64`.
    *
    * This is used to avoid duplicate publications, which can occur when a Kotlin Multiplatform
    * project is published in CI/CD on different host machines (Linux, Windows, and macOS).
    *
    * For example, by including `jvm` in the values when publishing on Linux, but omitting `jvm` on
    * Windows and macOS, this results in any Kotlin/JVM publications only being published once.
    */
   val enabledPublicationNamePrefixes: Provider<Set<String>> =
      kapperSetting("enabledPublicationNamePrefixes", "KotlinMultiplatform,Jvm,Js,iOS,macOS,watchOS,tvOS,mingw")
         .map { enabledPlatforms ->
            enabledPlatforms
               .split(",")
               .map { it.trim() }
               .filter { it.isNotBlank() }
               .toSet()
         }

   private fun kapperSetting(name: String, default: String? = null) =
      providers.gradleProperty("kapper_$name")
         .orElse(providers.provider { default }) // workaround for https://github.com/gradle/gradle/issues/12388

   private fun kapperFlag(name: String, default: Boolean) =
      providers.gradleProperty("kapper_$name").map { it.toBoolean() }.orElse(default)

   companion object {
      const val EXTENSION_NAME = "kapperSettings"

      /**
       * Regex for matching the release version.
       *
       * If a version does not match this code it should be treated as a SNAPSHOT version.
       */
      val releaseVersionRegex = Regex("\\d+.\\d+.\\d+")
   }
}
