package com.tymex.kmp.demo.implementation

import com.tymex.kmp.demo.core.api.UserRepository
import com.tymex.kmp.demo.core.api.User
import com.tymex.kmp.demo.core.api.PaymentService
import com.tymex.kmp.demo.core.api.PaymentRequest
import com.tymex.kmp.demo.core.impl.UserRepositoryImpl
import com.tymex.kmp.demo.core.impl.UserRepositoryFactory
import com.tymex.kmp.demo.resources.Strings
import com.tymex.kmp.demo.resources.Images
import kotlinx.coroutines.flow.Flow

/**
 * Demo class showing implementation usage
 * 
 * This class demonstrates how to use dependencies with implementation:
 * 1. All dependencies are available during compilation and runtime
 * 2. Concrete implementations are directly accessible
 * 3. No dependency injection required
 * 4. Final binary will be larger (all dependencies included)
 * 5. Transitive dependencies are automatically included
 */
class ImplementationDemo {
    
    // Direct instantiation - no dependency injection needed
    private val userRepository: UserRepository = UserRepositoryFactory.create()
    
    // Could also instantiate directly
    private val userRepositoryDirect: UserRepository = UserRepositoryImpl()
    
    /**
     * User management operations
     * Uses concrete UserRepository implementation
     */
    suspend fun getUserProfile(userId: String): User? {
        // Implementation is directly available
        // No runtime dependency injection needed
        return userRepository.getUserById(userId)
    }
    
    /**
     * Get all users with direct implementation access
     */
    fun getAllUsers(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }
    
    /**
     * Advanced user operations
     * Can access implementation-specific features
     */
    suspend fun performAdvancedUserOperations() {
        // Can use factory methods
        val configuredRepo = UserRepositoryFactory.create(
            com.tymex.kmp.demo.core.impl.RepositoryConfig(
                enableCaching = true,
                cacheSize = 200,
                networkTimeout = 10000
            )
        )
        
        // Use configured repository
        val users = configuredRepo.getAllUsers()
        // Process users...
    }
    
    /**
     * Resource usage demonstration
     * Direct access to resource identifiers
     */
    fun getResourceIdentifiers(): ResourceInfo {
        // Resource identifiers directly available
        // No dependency injection needed
        return ResourceInfo(
            loginTitle = Strings.LOGIN_TITLE,
            appIcon = Images.APP_ICON,
            userIcon = Images.IC_USER,
            paymentIcon = Images.IC_CREDIT_CARD,
            successMessage = Strings.PAYMENT_SUCCESS_MESSAGE,
            // Can access all resource categories
            allImages = getAllImageResources(),
            allStrings = getAllStringResources()
        )
    }
    
    /**
     * Demonstrate full feature access
     * With implementation, all features are directly available
     */
    suspend fun demonstrateFullFeatureAccess() {
        // Direct access to implementation
        val user = User(
            id = "impl-demo-user",
            name = "Implementation Demo User",
            email = "impl-demo@tymex.com",
            metadata = mapOf(
                "source" to "implementation-demo",
                "timestamp" to System.currentTimeMillis().toString()
            )
        )
        
        // Save user
        val saveResult = userRepository.saveUser(user)
        saveResult.onSuccess {
            println("User saved successfully")
            
            // Can immediately use other operations
            val retrievedUser = userRepository.getUserById(user.id)
            println("Retrieved user: $retrievedUser")
            
            // Search functionality
            val searchResults = userRepository.searchUsers("demo")
            println("Search results: ${searchResults.size} users found")
        }
    }
    
    /**
     * Resource validation with full access
     */
    fun validateAllResources(): ValidationReport {
        val report = ValidationReport()
        
        // Can validate all resources since they're all available
        getAllImageResources().forEach { imageId ->
            report.addImageCheck(imageId)
        }
        
        getAllStringResources().forEach { stringId ->
            report.addStringCheck(stringId)
        }
        
        return report
    }
    
    /**
     * Performance comparison operations
     */
    suspend fun performBenchmarkOperations(): BenchmarkResults {
        val startTime = System.currentTimeMillis()
        
        // Perform multiple operations
        val user1 = userRepository.getUserById("1")
        val user2 = userRepository.getUserById("2")
        val allUsers = userRepository.getAllUsers()
        val searchResults = userRepository.searchUsers("test")
        
        val endTime = System.currentTimeMillis()
        
        return BenchmarkResults(
            operationCount = 4,
            totalTimeMs = endTime - startTime,
            averageTimeMs = (endTime - startTime) / 4.0,
            memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        )
    }
    
    private fun getAllImageResources(): List<String> {
        return listOf(
            Images.APP_ICON, Images.APP_LOGO, Images.IC_USER,
            Images.IC_SETTINGS, Images.IC_CREDIT_CARD, Images.BG_LOGIN
        )
    }
    
    private fun getAllStringResources(): List<String> {
        return listOf(
            Strings.APP_NAME, Strings.LOGIN_TITLE, Strings.USER_PROFILE,
            Strings.PAYMENT_TITLE, Strings.ACTION_OK, Strings.ERROR_NETWORK
        )
    }
}

/**
 * Enhanced resource information with full access
 */
data class ResourceInfo(
    val loginTitle: String,
    val appIcon: String,
    val userIcon: String,
    val paymentIcon: String,
    val successMessage: String,
    val allImages: List<String>,
    val allStrings: List<String>
)

/**
 * Enhanced validation report with full resource access
 */
class ValidationReport {
    private val imageChecks = mutableListOf<ResourceCheck>()
    private val stringChecks = mutableListOf<ResourceCheck>()
    
    fun addImageCheck(resourceId: String) {
        imageChecks.add(ResourceCheck("Image", resourceId, true))
    }
    
    fun addStringCheck(resourceId: String) {
        stringChecks.add(ResourceCheck("String", resourceId, true))
    }
    
    fun getAllChecks(): List<ResourceCheck> = imageChecks + stringChecks
    
    fun getSummary(): ValidationSummary {
        return ValidationSummary(
            totalChecks = imageChecks.size + stringChecks.size,
            imageChecks = imageChecks.size,
            stringChecks = stringChecks.size,
            passedChecks = getAllChecks().count { it.isValid }
        )
    }
    
    data class ResourceCheck(
        val type: String,
        val resourceId: String,
        val isValid: Boolean
    )
    
    data class ValidationSummary(
        val totalChecks: Int,
        val imageChecks: Int,
        val stringChecks: Int,
        val passedChecks: Int
    )
}

/**
 * Benchmark results for performance comparison
 */
data class BenchmarkResults(
    val operationCount: Int,
    val totalTimeMs: Long,
    val averageTimeMs: Double,
    val memoryUsage: Long
)

/**
 * Factory for creating ImplementationDemo
 * No dependency injection needed - everything is directly available
 */
object ImplementationDemoFactory {
    
    /**
     * Create demo instance - no parameters needed
     */
    fun create(): ImplementationDemo {
        return ImplementationDemo()
    }
    
    /**
     * Create multiple instances for testing
     */
    fun createMultiple(count: Int): List<ImplementationDemo> {
        return (1..count).map { ImplementationDemo() }
    }
}
