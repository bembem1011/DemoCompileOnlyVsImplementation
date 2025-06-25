package com.tymex.kmp.demo.compileonly

import com.tymex.kmp.demo.core.api.UserRepository
import com.tymex.kmp.demo.core.api.User
import com.tymex.kmp.demo.core.api.PaymentService
import com.tymex.kmp.demo.core.api.PaymentRequest
import com.tymex.kmp.demo.resources.Strings
import com.tymex.kmp.demo.resources.Images
import kotlinx.coroutines.flow.Flow

/**
 * Demo class showing compileOnly usage
 * 
 * This class demonstrates how to use dependencies with compileOnly:
 * 1. Interfaces are available during compilation
 * 2. Resource identifiers are available during compilation
 * 3. Implementations must be provided at runtime
 * 4. Final binary will be smaller (dependencies not included)
 */
class CompileOnlyDemo(
    // Dependencies injected at runtime - not included in binary
    private val userRepository: UserRepository,
    private val paymentService: PaymentService
) {
    
    /**
     * User management operations
     * Uses UserRepository interface (compileOnly dependency)
     */
    suspend fun getUserProfile(userId: String): User? {
        // Interface is available during compilation
        // Implementation must be provided at runtime
        return userRepository.getUserById(userId)
    }
    
    /**
     * Get all users
     * Returns Flow from interface
     */
    fun getAllUsers(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }
    
    /**
     * Process payment operation
     * Uses PaymentService interface (compileOnly dependency)
     */
    suspend fun processPayment(request: PaymentRequest) {
        // Interface available during compilation
        // Implementation injected at runtime
        val result = paymentService.processPayment(request)
        
        // Handle result...
        when (result) {
            is com.tymex.kmp.demo.core.api.PaymentResult.Success -> {
                println("Payment successful: ${result.paymentId}")
            }
            is com.tymex.kmp.demo.core.api.PaymentResult.Failed -> {
                println("Payment failed: ${result.errorMessage}")
            }
            is com.tymex.kmp.demo.core.api.PaymentResult.Pending -> {
                println("Payment pending: ${result.paymentId}")
            }
        }
    }
    
    /**
     * Resource usage demonstration
     * Uses resource identifiers (compileOnly dependency)
     */
    fun getResourceIdentifiers(): ResourceInfo {
        // Resource identifiers available during compilation
        // Actual resources loaded by platform-specific mechanisms
        return ResourceInfo(
            loginTitle = Strings.LOGIN_TITLE,
            appIcon = Images.APP_ICON,
            userIcon = Images.IC_USER,
            paymentIcon = Images.IC_CREDIT_CARD,
            successMessage = Strings.PAYMENT_SUCCESS_MESSAGE
        )
    }
    
    /**
     * Demonstrate compile-time type safety
     * Even with compileOnly, we get full type checking
     */
    suspend fun demonstrateTypeSafety() {
        // Compiler knows about User type
        val user = User(
            id = "demo-user",
            name = "Demo User",
            email = "demo@tymex.com"
        )
        
        // Compiler validates method signatures
        val result = userRepository.saveUser(user)
        
        // Compiler knows about Result type
        result.onSuccess { 
            println("User saved successfully")
        }.onError { error ->
            println("Failed to save user: ${error.message}")
        }
    }
    
    /**
     * Show resource validation at compile time
     */
    fun validateResources(): ValidationReport {
        val report = ValidationReport()
        
        // These checks happen at compile time
        report.addCheck("Login title resource", Strings.LOGIN_TITLE)
        report.addCheck("App icon resource", Images.APP_ICON)
        report.addCheck("User icon resource", Images.IC_USER)
        
        return report
    }
}

/**
 * Resource information data class
 */
data class ResourceInfo(
    val loginTitle: String,
    val appIcon: String,
    val userIcon: String,
    val paymentIcon: String,
    val successMessage: String
)

/**
 * Validation report for compile-time checks
 */
class ValidationReport {
    private val checks = mutableListOf<CheckResult>()
    
    fun addCheck(description: String, resourceId: String) {
        checks.add(CheckResult(description, resourceId, true))
    }
    
    fun getResults(): List<CheckResult> = checks.toList()
    
    data class CheckResult(
        val description: String,
        val resourceId: String,
        val isValid: Boolean
    )
}

/**
 * Factory for creating CompileOnlyDemo with dependency injection
 * This shows how runtime dependencies are provided
 */
object CompileOnlyDemoFactory {
    
    /**
     * Create demo instance with injected dependencies
     * In real app, this would use DI framework like Koin, Dagger, etc.
     */
    fun create(
        userRepository: UserRepository,
        paymentService: PaymentService
    ): CompileOnlyDemo {
        return CompileOnlyDemo(userRepository, paymentService)
    }
    
    /**
     * Create demo with mock dependencies for testing
     */
    fun createForTesting(): CompileOnlyDemo {
        return CompileOnlyDemo(
            userRepository = MockUserRepository(),
            paymentService = MockPaymentService()
        )
    }
}

/**
 * Mock implementations for testing
 * These would typically be in test sources
 */
private class MockUserRepository : UserRepository {
    override suspend fun getUserById(userId: String): User? = null
    override fun getAllUsers(): Flow<List<User>> = kotlinx.coroutines.flow.flowOf(emptyList())
    override suspend fun saveUser(user: User) = com.tymex.kmp.demo.core.api.Result.Success(Unit)
    override suspend fun deleteUser(userId: String) = com.tymex.kmp.demo.core.api.Result.Success(Unit)
    override suspend fun searchUsers(query: String): List<User> = emptyList()
}

private class MockPaymentService : PaymentService {
    override suspend fun processPayment(request: PaymentRequest) = 
        com.tymex.kmp.demo.core.api.PaymentResult.Success("mock-id", "mock-tx", request.amount, request.currency, 0L)
    override fun getPaymentHistory(userId: String) = kotlinx.coroutines.flow.flowOf(emptyList())
    override suspend fun validatePaymentMethod(paymentMethod: com.tymex.kmp.demo.core.api.PaymentMethod) = 
        com.tymex.kmp.demo.core.api.ValidationResult(true)
    override suspend fun refundPayment(paymentId: String, amount: Double?) = 
        com.tymex.kmp.demo.core.api.RefundResult.Success("mock-refund", amount ?: 0.0, 0L)
}
