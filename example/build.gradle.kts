plugins {
   kotlin("multiplatform")
}

kotlin {
   jvm()

   sourceSets {
      commonMain {
         dependencies {
            implementation(projects.mikromCore)
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
         }
      }
   }
}

tasks.named("compileKotlinJvm").configure {
   outputs.upToDateWhen { false }
}

dependencies {
   kotlinCompilerPluginClasspath(projects.mikromCompilerPlugin)
}
