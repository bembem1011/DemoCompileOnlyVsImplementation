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
            // API dependency - this could be compileOnly in consuming modules
            implementation(project(":core-api"))
            
            // Implementation dependencies - these will be included in final binary
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            
            // HTTP client for network operations
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
            
            // Database
            implementation("app.cash.sqldelight:runtime:2.0.1")
            implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        }
        
        androidMain.dependencies {
            implementation("io.ktor:ktor-client-android:2.3.7")
            implementation("app.cash.sqldelight:android-driver:2.0.1")
        }
        
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:2.3.7")
            implementation("app.cash.sqldelight:native-driver:2.0.1")
        }
    }
}

android {
    namespace = "com.tymex.kmp.demo.core.impl"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 24
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
