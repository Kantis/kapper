rootProject.name = "mikrom"

includeBuild("libs")
includeBuild("mikrom-compiler-plugin")
includeBuild("mikrom-gradle-plugin")
includeBuild("example")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
