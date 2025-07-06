package io.github.kantis.mikrom.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

public class MikromGradlePlugin : KotlinCompilerPluginSupportPlugin {
   override fun apply(target: Project): Unit =
      with(target) {
         extensions.create("template", MikromGradleExtension::class.java)
      }

   override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

   override fun getCompilerPluginId(): String = "mikrom"

   override fun getPluginArtifact(): SubpluginArtifact =
      SubpluginArtifact(
         groupId = "io.github.kantis",
         artifactId = "mikrom-compiler-plugin",
         version = "0.1.0-SNAPSHOT",
      )

//   override fun getPluginArtifactForNative(): SubpluginArtifact =
//      SubpluginArtifact(
//         groupId = "foo",
//         artifactId = "bar-native",
//         version = "1.1.0",
//      )
//
   override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
      val project = kotlinCompilation.target.project
      val extension = project.extensions.getByType(MikromGradleExtension::class.java)
      return project.provider {
         listOf(
            // Nothing yet..
//            SubpluginOption(key = "string", value = extension.stringProperty.get()),
//            SubpluginOption(key = "file", value = extension.fileProperty.get().asFile.path),
         )
      }
   }
}
