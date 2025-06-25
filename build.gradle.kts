plugins {
    // Kotlin Multiplatform
    kotlin("multiplatform") version "1.9.22" apply false
    kotlin("android") version "1.9.22" apply false
    
    // Android
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    
    // Serialization
    kotlin("plugin.serialization") version "1.9.22" apply false
}

allprojects {
    group = "com.tymex.kmp.demo"
    version = "1.0.0"
}

// Common configurations
subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs += listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
        }
    }
}

// Build time measurement task
tasks.register("measureBuildTime") {
    group = "analysis"
    description = "Measures build time for different dependency configurations"
    
    doLast {
        println("=== Build Time Analysis ===")
        println("Check analysis/build-time-results.md for detailed results")
    }
}

// Binary size analysis task
tasks.register("analyzeBinarySize") {
    group = "analysis"
    description = "Analyzes binary size impact of different dependency configurations"
    
    doLast {
        println("=== Binary Size Analysis ===")
        println("Check analysis/binary-size-results.md for detailed results")
    }
}
