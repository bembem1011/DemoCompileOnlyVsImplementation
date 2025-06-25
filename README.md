# CompileOnly vs Implementation POC Demo

## 🎯 Overview

This POC demonstrates the differences between `compileOnly` and `implementation` dependency configurations in Kotlin Multiplatform (KMP) projects, specifically focusing on:

- **Android**: Traditional Android mechanisms
- **iOS**: Framework generation for native integration
- **Multi-module architecture**: Complex dependency scenarios

## 📋 Table of Contents

1. [Project Structure](#project-structure)
2. [Demo Scenarios](#demo-scenarios)
3. [Key Differences](#key-differences)
4. [Performance Analysis](#performance-analysis)
5. [Backward Compatibility](#backward-compatibility)
6. [Decision Matrix](#decision-matrix)
7. [Running the Demo](#running-the-demo)

## 🏗️ Project Structure

```
kmp-dependency-demo/
├── shared/                          # KMP shared module
│   ├── src/commonMain/
│   ├── src/androidMain/
│   └── src/iosMain/
├── core-api/                        # API definitions (compileOnly candidate)
├── core-impl/                       # Implementation (implementation)
├── feature-auth/                    # Feature module
├── feature-payment/                 # Feature module
├── resources-module/                # Resources (current compileOnly usage)
├── android-app/                     # Android application
├── ios-framework/                   # iOS framework target
├── presentation/                    # Presentation slides
└── analysis/                        # Performance analysis results
```

## 🎭 Demo Scenarios

### Scenario 1: API vs Implementation Split
- **core-api**: Interface definitions (compileOnly)
- **core-impl**: Concrete implementations (implementation)

### Scenario 2: Feature Modules
- **feature-auth**: Authentication module
- **feature-payment**: Payment processing module

### Scenario 3: Resource Modules (Current Usage)
- **resources-module**: Images, strings, assets (compileOnly)

### Scenario 4: Transitive Dependencies
- Complex dependency chains showing impact

## 🔍 Key Differences

| Aspect | compileOnly | implementation |
|--------|-------------|----------------|
| **Compile Time** | Available during compilation | Available during compilation |
| **Runtime** | NOT included in final binary | Included in final binary |
| **Transitive Dependencies** | NOT propagated | Propagated to consumers |
| **Binary Size** | Smaller (dependencies excluded) | Larger (dependencies included) |
| **Use Case** | APIs, interfaces, compile-time only | Full functionality needed at runtime |

## 📊 Performance Analysis

### Build Time Comparison
- **compileOnly**: Faster incremental builds
- **implementation**: Slower due to transitive dependency resolution

### Binary Size Impact
- **Android APK**: Size differences with each approach
- **iOS Framework**: Framework size variations

### Memory Usage
- **Compile-time**: Memory usage during build
- **Runtime**: Memory footprint differences

## 🔄 Backward Compatibility

### Android Compatibility
- **Gradle Plugin**: Minimum required versions
- **AGP Compatibility**: Android Gradle Plugin requirements
- **API Level**: Minimum SDK considerations

### iOS Compatibility
- **Xcode**: Minimum Xcode version requirements
- **iOS Deployment**: Target iOS version compatibility
- **Swift Interop**: Swift version compatibility

### KMP Compatibility
- **Kotlin Version**: Minimum Kotlin version
- **KMP Plugin**: Multiplatform plugin versions
- **Gradle**: Minimum Gradle version

## 🎯 Decision Matrix

### When to Use `compileOnly`

✅ **Use compileOnly when:**
- Defining APIs/interfaces that implementations will provide
- Working with annotation processors
- Including resources that should not be bundled
- Creating plugin architectures
- Avoiding dependency conflicts
- Reducing binary size is critical

### When to Use `implementation`

✅ **Use implementation when:**
- Need functionality at runtime
- Standard feature modules
- Utility libraries
- Third-party dependencies
- Default choice for most dependencies

## 🚀 Running the Demo

### Prerequisites
- Kotlin 1.9.0+
- Gradle 8.0+
- Android Studio Hedgehog+
- Xcode 15+ (for iOS)

### Setup
```bash
# Clone and setup
git clone <repository>
cd kmp-dependency-demo

# Build all modules
./gradlew build

# Run Android app
./gradlew :android-app:installDebug

# Build iOS framework
./gradlew :shared:assembleXCFramework
```

### Analysis Scripts
```bash
# Run build time analysis
./scripts/analyze-build-time.sh

# Analyze binary sizes
./scripts/analyze-binary-size.sh

# Generate compatibility report
./scripts/check-compatibility.sh
```

## 📈 Results Summary

### Current State Analysis
- **All modules use `implementation`** except resources
- **Resources module uses `compileOnly`** for images
- **No API/implementation separation** in business logic

### Recommendations
1. **Introduce API modules** with `compileOnly` for better architecture
2. **Keep implementation modules** with `implementation`
3. **Maintain resource strategy** with `compileOnly`
4. **Gradual migration** to avoid breaking changes

## 📚 Additional Resources

- [Presentation Slides](./presentation/slides.md)
- [Detailed Analysis](./analysis/detailed-analysis.md)
- [Migration Guide](./docs/migration-guide.md)
- [Best Practices](./docs/best-practices.md)

---

**Built for TymeX KMP Architecture Decision** 🏗️
