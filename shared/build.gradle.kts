plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
            // Demo: Mixed dependency approach
            
            // CompileOnly for API definitions
            compileOnly(project(":core-api"))
            
            // Implementation for concrete functionality
            implementation(project(":core-impl"))
            
            // CompileOnly for resources (current successful pattern)
            compileOnly(project(":resources-module"))
            
            // Demo modules for comparison
            implementation(project(":demo-compile-only"))
            implementation(project(":demo-implementation"))
            
            // Standard KMP dependencies
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        }
        
        androidMain.dependencies {
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
        }
        
        iosMain.dependencies {
            // iOS-specific dependencies
        }
    }
}

android {
    namespace = "com.tymex.kmp.demo.shared"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 24
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// Task to build iOS framework
tasks.register("assembleXCFramework") {
    group = "build"
    description = "Assemble XCFramework for iOS"
    dependsOn("linkDebugFrameworkIosArm64", "linkDebugFrameworkIosX64", "linkDebugFrameworkIosSimulatorArm64")
}
