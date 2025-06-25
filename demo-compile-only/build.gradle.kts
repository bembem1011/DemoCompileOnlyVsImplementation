plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    sourceSets {
        commonMain.dependencies {
            // DEMO: Using compileOnly for API dependencies
            // These will be available during compilation but NOT in final binary
            compileOnly(project(":core-api"))
            compileOnly(project(":resources-module"))
            
            // Only essential runtime dependencies
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            
            // Note: Implementation must be provided by the consuming module
            // or injected at runtime for this to work
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
            // For testing, we need the actual implementations
            implementation(project(":core-api"))
            implementation(project(":core-impl"))
        }
    }
}

android {
    namespace = "com.tymex.kmp.demo.compileonly"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 24
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// Task to analyze binary size with compileOnly approach
tasks.register("analyzeBinarySizeCompileOnly") {
    group = "analysis"
    description = "Analyze binary size impact of compileOnly dependencies"
    
    doLast {
        println("=== CompileOnly Binary Size Analysis ===")
        println("This module uses compileOnly for:")
        println("- core-api: Interface definitions")
        println("- resources-module: Resource identifiers")
        println("Expected result: Smaller binary size, runtime dependency injection required")
    }
}
