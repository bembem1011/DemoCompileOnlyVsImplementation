pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kmp-dependency-demo"

// Core modules
include(":shared")
include(":core-api")
include(":core-impl")

// Feature modules
include(":feature-auth")
include(":feature-payment")

// Resource module (current compileOnly usage)
include(":resources-module")

// Platform-specific modules
include(":android-app")

// Analysis and demo modules
include(":demo-compile-only")
include(":demo-implementation")
include(":performance-analyzer")
