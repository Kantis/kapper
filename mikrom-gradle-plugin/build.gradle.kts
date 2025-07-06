plugins {
   id("org.jetbrains.kotlin.jvm") version "2.1.20"
   id("com.github.gmazzo.buildconfig") version "5.6.7"
}

buildConfig {
   val project = project(":kotlin-ir-plugin")
   packageName(project.group.toString())
   buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.extra["kotlin_plugin_id"]}\"")
   buildConfigField("String", "KOTLIN_PLUGIN_GROUP", "\"${project.group}\"")
   buildConfigField("String", "KOTLIN_PLUGIN_NAME", "\"${project.name}\"")
   buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${project.version}\"")
}

