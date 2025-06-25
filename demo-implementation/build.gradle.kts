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
            // DEMO: Using implementation for all dependencies
            // These will be available during compilation AND in final binary
            implementation(project(":core-api"))
            implementation(project(":core-impl"))
            implementation(project(":resources-module"))
            
            // All transitive dependencies will be included
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            
            // Note: All dependencies and their transitive dependencies
            // will be included in the final binary
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.tymex.kmp.demo.implementation"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 24
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// Task to analyze binary size with implementation approach
tasks.register("analyzeBinarySizeImplementation") {
    group = "analysis"
    description = "Analyze binary size impact of implementation dependencies"
    
    doLast {
        println("=== Implementation Binary Size Analysis ===")
        println("This module uses implementation for:")
        println("- core-api: Interface definitions")
        println("- core-impl: Concrete implementations")
        println("- resources-module: Resource identifiers")
        println("Expected result: Larger binary size, no runtime dependency injection needed")
    }
}
