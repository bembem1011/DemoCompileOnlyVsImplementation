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
            // Only essential dependencies for API definitions
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            
            // Note: This module is designed to be used with compileOnly
            // It contains only interfaces and data classes
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        
        androidMain.dependencies {
            // Android-specific API dependencies (minimal)
        }
        
        iosMain.dependencies {
            // iOS-specific API dependencies (minimal)
        }
    }
}

android {
    namespace = "com.tymex.kmp.demo.core.api"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 24
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
