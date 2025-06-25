# Migration Guide: CompileOnly vs Implementation

## 🎯 Overview

This guide provides step-by-step instructions for migrating from an all-implementation approach to a strategic mix of compileOnly and implementation dependencies in your KMP project.

## 📋 Prerequisites

Before starting the migration:

- [ ] Kotlin 1.8.0+ (for stable compileOnly support)
- [ ] Gradle 7.4+ (for modern dependency management)
- [ ] Android Gradle Plugin 7.4+ (for compatibility)
- [ ] Team familiarity with dependency injection concepts
- [ ] Backup of current working codebase

## 🚦 Migration Strategy

### Phase 1: Foundation (Week 1-2)

#### 1.1 Set Up Measurement Tools

```bash
# Add build time measurement
./gradlew build --profile

# Set up binary size tracking
./scripts/analyze-binary-size.sh

# Establish baselines
./scripts/analyze-build-time.sh
```

#### 1.2 Choose Dependency Injection Framework

**Recommended: Koin (KMP-friendly)**

```kotlin
// In shared/build.gradle.kts
commonMain.dependencies {
    implementation("io.insert-koin:koin-core:3.5.0")
}

androidMain.dependencies {
    implementation("io.insert-koin:koin-android:3.5.0")
}
```

#### 1.3 Document Current Architecture

Create an inventory of your modules:

```
Current Modules:
├── feature-auth (implementation) → Candidate for API split
├── feature-payment (implementation) → Candidate for API split  
├── core-network (implementation) → Keep as implementation
├── core-database (implementation) → Keep as implementation
├── resources (compileOnly) → Already optimized ✅
└── shared (implementation) → Mixed approach
```

### Phase 2: Pilot Module (Week 3-4)

#### 2.1 Choose Pilot Module

Select a module with:
- Clear interface boundaries
- Minimal external dependencies
- Non-critical functionality
- Active development

**Example: feature-auth**

#### 2.2 Create API Module

```bash
# Create new API module
mkdir feature-auth-api
```

```kotlin
// feature-auth-api/build.gradle.kts
plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    // ... platform configuration
    
    sourceSets {
        commonMain.dependencies {
            // Minimal dependencies - only interfaces
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        }
    }
}
```

#### 2.3 Extract Interfaces

```kotlin
// feature-auth-api/src/commonMain/kotlin/AuthService.kt
interface AuthService {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun logout(): Result<Unit>
    fun getCurrentUser(): Flow<User?>
}

// Move data classes to API module
data class AuthResult(...)
data class User(...)
```

#### 2.4 Update Implementation Module

```kotlin
// feature-auth-impl/build.gradle.kts
kotlin {
    sourceSets {
        commonMain.dependencies {
            // Use implementation for API (will be compileOnly in consumers)
            implementation(project(":feature-auth-api"))
            
            // Keep implementation dependencies
            implementation(project(":core-network"))
            implementation(project(":core-database"))
        }
    }
}
```

#### 2.5 Set Up Dependency Injection

```kotlin
// shared/src/commonMain/kotlin/di/AuthModule.kt
val authModule = module {
    single<AuthService> { AuthServiceImpl(get(), get()) }
}

// Application setup
fun initKoin() {
    startKoin {
        modules(authModule)
    }
}
```

#### 2.6 Update Consumers

```kotlin
// In consuming modules
dependencies {
    // Use compileOnly for API
    compileOnly(project(":feature-auth-api"))
    
    // Implementation provided by DI
}

// Usage with DI
class SomeFeature(private val authService: AuthService) {
    // Use interface, implementation injected
}
```

### Phase 3: Validation (Week 5)

#### 3.1 Measure Impact

```bash
# Compare build times
./scripts/analyze-build-time.sh

# Compare binary sizes  
./scripts/analyze-binary-size.sh

# Check dependency trees
./gradlew :android-app:dependencies
```

#### 3.2 Test Thoroughly

```kotlin
// Test with DI
class AuthServiceTest {
    @Test
    fun testWithMockDI() {
        val testModule = module {
            single<AuthService> { MockAuthService() }
        }
        
        startKoin { modules(testModule) }
        
        // Test with injected mock
    }
}
```

#### 3.3 Validate on All Platforms

- [ ] Android app builds and runs
- [ ] iOS framework generates correctly
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Performance acceptable

### Phase 4: Gradual Expansion (Week 6-12)

#### 4.1 Migration Priority Matrix

| Module | Priority | Complexity | Risk | Timeline |
|--------|----------|------------|------|----------|
| feature-payment | High | Medium | Low | Week 6-7 |
| feature-profile | High | Low | Low | Week 8 |
| core-utils | Medium | Low | Medium | Week 9-10 |
| legacy-module | Low | High | High | Week 11-12 |

#### 4.2 Module-by-Module Migration

For each module:

1. **Analyze dependencies**
   ```bash
   ./gradlew :module-name:dependencies
   ```

2. **Create API module**
   ```bash
   mkdir module-name-api
   # Copy interfaces and data classes
   ```

3. **Update build scripts**
   ```kotlin
   // Update consumers to use compileOnly
   compileOnly(project(":module-name-api"))
   ```

4. **Update DI configuration**
   ```kotlin
   val moduleNameModule = module {
       single<ModuleInterface> { ModuleImplementation() }
   }
   ```

5. **Test and validate**
   ```bash
   ./gradlew test
   ./gradlew build
   ```

### Phase 5: Optimization (Week 13-16)

#### 5.1 Fine-tune Build Configuration

```kotlin
// Optimize Gradle configuration
android {
    packagingOptions {
        // Exclude duplicate files from compileOnly modules
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}
```

#### 5.2 Advanced DI Patterns

```kotlin
// Lazy injection for performance
val heavyModule = module {
    single<HeavyService> { HeavyServiceImpl() }
}

// Scoped injection
val scopedModule = module {
    scope<UserSession> {
        scoped<UserService> { UserServiceImpl() }
    }
}
```

#### 5.3 Build Optimization

```kotlin
// Parallel builds
org.gradle.parallel=true
org.gradle.caching=true

// Memory optimization
org.gradle.jvmargs=-Xmx4g -XX:+UseParallelGC
```

## 🛠️ Tools and Scripts

### Build Time Monitoring

```bash
# Add to CI/CD pipeline
./scripts/analyze-build-time.sh
if [ $build_time -gt $threshold ]; then
    echo "Build time regression detected"
    exit 1
fi
```

### Binary Size Monitoring

```bash
# Add to release pipeline
./scripts/analyze-binary-size.sh
if [ $apk_size -gt $max_size ]; then
    echo "APK size limit exceeded"
    exit 1
fi
```

### Dependency Validation

```bash
# Validate compileOnly usage
./gradlew :module:dependencies --configuration compileClasspath | grep compileOnly
```

## 🚨 Common Pitfalls and Solutions

### 1. Runtime ClassNotFoundException

**Problem:** CompileOnly dependency not available at runtime

**Solution:**
```kotlin
// Ensure implementation is provided via DI
val module = module {
    single<ApiInterface> { ApiImplementation() }
}
```

### 2. Circular Dependencies

**Problem:** API and implementation modules depend on each other

**Solution:**
```kotlin
// API module should have no implementation dependencies
// Implementation module depends on API module only
api-module → (no dependencies)
impl-module → api-module
```

### 3. Build Cache Issues

**Problem:** Inconsistent builds with compileOnly

**Solution:**
```bash
# Clear build cache when switching approaches
./gradlew clean
rm -rf ~/.gradle/caches
```

### 4. iOS Framework Generation Issues

**Problem:** CompileOnly modules not included in iOS framework

**Solution:**
```kotlin
// Ensure shared module includes implementations
kotlin {
    sourceSets {
        commonMain.dependencies {
            // Include implementations in shared module
            implementation(project(":feature-impl"))
        }
    }
}
```

## 📊 Success Metrics

### Technical Metrics

- [ ] Build time improvement: Target 20-30%
- [ ] Binary size reduction: Target 15-25%
- [ ] Dependency count reduction: Target 30-50%
- [ ] Memory usage improvement: Target 10-20%

### Quality Metrics

- [ ] No runtime errors
- [ ] All tests passing
- [ ] Performance maintained or improved
- [ ] Code coverage maintained

### Team Metrics

- [ ] Developer satisfaction maintained
- [ ] Onboarding time acceptable
- [ ] Debugging complexity manageable
- [ ] Maintenance overhead acceptable

## 🔄 Rollback Plan

If migration causes issues:

### Immediate Rollback (< 1 hour)

```bash
# Revert to implementation for problematic module
git checkout main -- module-name/build.gradle.kts
./gradlew clean build
```

### Partial Rollback (< 1 day)

```kotlin
// Change compileOnly back to implementation temporarily
dependencies {
    implementation(project(":module-api")) // was compileOnly
}
```

### Full Rollback (< 1 week)

```bash
# Revert entire migration
git revert <migration-commits>
# Remove DI framework
# Restore original build scripts
```

## 📚 Additional Resources

- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Gradle Dependency Management](https://docs.gradle.org/current/userguide/dependency_management.html)
- [Koin Documentation](https://insert-koin.io/)
- [Android App Bundle Optimization](https://developer.android.com/guide/app-bundle)

## 🎯 Next Steps

After completing migration:

1. **Monitor long-term impact** - Track metrics over 3-6 months
2. **Establish best practices** - Document patterns for new modules
3. **Train team** - Ensure everyone understands new architecture
4. **Automate validation** - Add checks to CI/CD pipeline
5. **Plan next optimizations** - Consider other build optimizations

---

*This migration guide provides a structured approach to adopting compileOnly dependencies while minimizing risk and maximizing benefits.*
