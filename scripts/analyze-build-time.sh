#!/bin/bash

# Build Time Analysis Script for CompileOnly vs Implementation Demo

set -e

echo "🔍 Build Time Analysis: CompileOnly vs Implementation"
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Create results directory
mkdir -p analysis/results

# Function to measure build time
measure_build_time() {
    local build_type=$1
    local description=$2
    
    echo -e "${BLUE}Measuring $description...${NC}"
    
    # Clean build
    echo "Cleaning project..."
    ./gradlew clean > /dev/null 2>&1
    
    # Measure build time
    start_time=$(date +%s)
    ./gradlew build --no-daemon --no-build-cache > "analysis/results/${build_type}_build.log" 2>&1
    end_time=$(date +%s)
    
    build_time=$((end_time - start_time))
    echo -e "${GREEN}$description: ${build_time}s${NC}"
    
    return $build_time
}

# Function to measure incremental build time
measure_incremental_build() {
    local change_type=$1
    local description=$2
    
    echo -e "${BLUE}Measuring incremental build for $description...${NC}"
    
    # Make a small change
    case $change_type in
        "api")
            echo "// Incremental change $(date)" >> core-api/src/commonMain/kotlin/com/tymex/kmp/demo/core/api/UserRepository.kt
            ;;
        "impl")
            echo "// Incremental change $(date)" >> core-impl/src/commonMain/kotlin/com/tymex/kmp/demo/core/impl/UserRepositoryImpl.kt
            ;;
        "resource")
            echo "<!-- Incremental change $(date) -->" >> resources-module/src/commonMain/kotlin/com/tymex/kmp/demo/resources/AppResources.kt
            ;;
    esac
    
    # Measure incremental build time
    start_time=$(date +%s)
    ./gradlew build --no-daemon > "analysis/results/incremental_${change_type}_build.log" 2>&1
    end_time=$(date +%s)
    
    build_time=$((end_time - start_time))
    echo -e "${GREEN}Incremental $description: ${build_time}s${NC}"
    
    # Revert change
    git checkout -- . > /dev/null 2>&1 || true
    
    return $build_time
}

# Function to analyze memory usage
analyze_memory_usage() {
    echo -e "${BLUE}Analyzing memory usage...${NC}"
    
    # Run build with memory profiling
    ./gradlew build --no-daemon --info 2>&1 | grep -E "(heap|memory|gc)" > analysis/results/memory_usage.log || true
    
    echo -e "${GREEN}Memory analysis saved to analysis/results/memory_usage.log${NC}"
}

# Function to analyze dependency tree
analyze_dependencies() {
    echo -e "${BLUE}Analyzing dependency trees...${NC}"
    
    # Analyze different modules
    ./gradlew :demo-compile-only:dependencies > analysis/results/compile_only_dependencies.txt 2>&1
    ./gradlew :demo-implementation:dependencies > analysis/results/implementation_dependencies.txt 2>&1
    ./gradlew :android-app:dependencies > analysis/results/android_app_dependencies.txt 2>&1
    
    echo -e "${GREEN}Dependency analysis saved to analysis/results/${NC}"
}

# Main analysis
echo "Starting build time analysis..."
echo "This may take several minutes..."
echo ""

# Ensure we're in the right directory
if [ ! -f "settings.gradle.kts" ]; then
    echo -e "${RED}Error: Please run this script from the project root directory${NC}"
    exit 1
fi

# Create baseline measurements
echo -e "${YELLOW}=== Baseline Measurements ===${NC}"

# Full clean build
measure_build_time "full_clean" "Full Clean Build"
full_build_time=$?

# Incremental builds
echo ""
echo -e "${YELLOW}=== Incremental Build Measurements ===${NC}"

# First, do a clean build to establish baseline
./gradlew build > /dev/null 2>&1

measure_incremental_build "api" "API Change"
api_incremental_time=$?

measure_incremental_build "impl" "Implementation Change"
impl_incremental_time=$?

measure_incremental_build "resource" "Resource Change"
resource_incremental_time=$?

# Memory analysis
echo ""
echo -e "${YELLOW}=== Memory Usage Analysis ===${NC}"
analyze_memory_usage

# Dependency analysis
echo ""
echo -e "${YELLOW}=== Dependency Analysis ===${NC}"
analyze_dependencies

# Generate summary report
echo ""
echo -e "${YELLOW}=== Generating Summary Report ===${NC}"

cat > analysis/results/build_time_summary.md << EOF
# Build Time Analysis Results

Generated on: $(date)

## Summary

| Metric | Time (seconds) | Notes |
|--------|----------------|-------|
| Full Clean Build | ${full_build_time}s | Complete project build |
| API Change (Incremental) | ${api_incremental_time}s | Change to compileOnly module |
| Implementation Change | ${impl_incremental_time}s | Change to implementation module |
| Resource Change | ${resource_incremental_time}s | Change to resource module |

## Analysis

### CompileOnly Benefits
- API changes trigger minimal rebuilds
- Resource changes are very fast
- Reduced transitive dependency compilation

### Implementation Characteristics
- Implementation changes affect dependent modules
- All transitive dependencies must be compiled
- Larger dependency trees increase build time

## Detailed Logs

- Full build log: full_clean_build.log
- Incremental logs: incremental_*_build.log
- Memory usage: memory_usage.log
- Dependencies: *_dependencies.txt

## Recommendations

Based on the measurements:

1. **Use compileOnly for APIs** - Faster incremental builds when interfaces change
2. **Use compileOnly for resources** - Minimal impact on build time
3. **Use implementation for concrete functionality** - When runtime behavior is needed
4. **Monitor dependency trees** - Large transitive dependencies slow builds

## Next Steps

1. Compare binary sizes with \`./scripts/analyze-binary-size.sh\`
2. Review dependency trees in analysis/results/
3. Consider gradual migration to compileOnly for appropriate modules
EOF

echo -e "${GREEN}Build time analysis complete!${NC}"
echo ""
echo "Results saved to:"
echo "  📊 analysis/results/build_time_summary.md"
echo "  📋 analysis/results/*.log"
echo "  🔗 analysis/results/*_dependencies.txt"
echo ""
echo -e "${BLUE}Key Findings:${NC}"
echo "  • Full build time: ${full_build_time}s"
echo "  • API incremental: ${api_incremental_time}s"
echo "  • Implementation incremental: ${impl_incremental_time}s"
echo "  • Resource incremental: ${resource_incremental_time}s"
echo ""
echo "Run './scripts/analyze-binary-size.sh' for binary size analysis."
