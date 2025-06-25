plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.tymex.kmp.demo.android"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.tymex.kmp.demo.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = "1.8"
    }
    
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // Shared KMP module
    implementation(project(":shared"))
    
    // DEMO: Different dependency approaches for comparison
    
    // CompileOnly approach - API only
    compileOnly(project(":core-api"))
    compileOnly(project(":resources-module"))
    
    // Implementation approach - Full functionality
    implementation(project(":core-impl"))
    implementation(project(":demo-compile-only"))
    implementation(project(":demo-implementation"))
    
    // Android dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Task to measure APK size for comparison
tasks.register("measureApkSize") {
    group = "analysis"
    description = "Measure APK size for dependency comparison"
    dependsOn("assembleDebug", "assembleRelease")
    
    doLast {
        val debugApk = file("build/outputs/apk/debug/android-app-debug.apk")
        val releaseApk = file("build/outputs/apk/release/android-app-release.apk")
        
        println("=== APK Size Analysis ===")
        if (debugApk.exists()) {
            println("Debug APK: ${debugApk.length() / 1024 / 1024} MB")
        }
        if (releaseApk.exists()) {
            println("Release APK: ${releaseApk.length() / 1024 / 1024} MB")
        }
    }
}

// Task to analyze dependency tree
tasks.register("analyzeDependencies") {
    group = "analysis"
    description = "Analyze dependency tree for comparison"
    
    doLast {
        println("=== Dependency Analysis ===")
        println("Run './gradlew :android-app:dependencies' for detailed dependency tree")
        println("CompileOnly dependencies will not appear in runtime classpath")
        println("Implementation dependencies will include all transitive dependencies")
    }
}
