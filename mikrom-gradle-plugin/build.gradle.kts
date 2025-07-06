plugins {
   id("org.jetbrains.kotlin.jvm") version "2.1.20"
   id("com.github.gmazzo.buildconfig") version "5.6.7"
}

buildConfig {
   packageName(project.group.toString())
//   buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.extra["kotlin_plugin_id"]}\"")
   buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"io.github.kantis.mikrom\"")
   buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"mikrom-compiler-plugin\"")
   buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"0.1.0-SNAPSHOT\"")
}
