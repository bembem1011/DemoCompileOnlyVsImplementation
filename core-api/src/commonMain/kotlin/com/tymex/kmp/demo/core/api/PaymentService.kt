package com.tymex.kmp.demo.core.api

import kotlinx.coroutines.flow.Flow

/**
 * Payment service interface - designed for compileOnly usage
 * 
 * This demonstrates how payment processing APIs can be defined
 * separately from their implementations for better architecture.
 */
interface PaymentService {
    
    /**
     * Process payment
     * @param request Payment request details
     * @return Payment result
     */
    suspend fun processPayment(request: PaymentRequest): PaymentResult
    
    /**
     * Get payment history
     * @param userId User ID
     * @return Flow of payment history
     */
    fun getPaymentHistory(userId: String): Flow<List<Payment>>
    
    /**
     * Validate payment method
     * @param paymentMethod Payment method to validate
     * @return Validation result
     */
    suspend fun validatePaymentMethod(paymentMethod: PaymentMethod): ValidationResult
    
    /**
     * Refund payment
     * @param paymentId Payment ID to refund
     * @param amount Refund amount (null for full refund)
     * @return Refund result
     */
    suspend fun refundPayment(paymentId: String, amount: Double? = null): RefundResult
}

/**
 * Payment request data
 */
@kotlinx.serialization.Serializable
data class PaymentRequest(
    val userId: String,
    val amount: Double,
    val currency: String,
    val paymentMethod: PaymentMethod,
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap()
)

/**
 * Payment method types
 */
@kotlinx.serialization.Serializable
sealed class PaymentMethod {
    @kotlinx.serialization.Serializable
    data class CreditCard(
        val cardNumber: String,
        val expiryMonth: Int,
        val expiryYear: Int,
        val cvv: String,
        val holderName: String
    ) : PaymentMethod()
    
    @kotlinx.serialization.Serializable
    data class BankTransfer(
        val accountNumber: String,
        val routingNumber: String,
        val bankName: String
    ) : PaymentMethod()
    
    @kotlinx.serialization.Serializable
    data class DigitalWallet(
        val walletType: String,
        val walletId: String
    ) : PaymentMethod()
}

/**
 * Payment result
 */
@kotlinx.serialization.Serializable
sealed class PaymentResult {
    @kotlinx.serialization.Serializable
    data class Success(
        val paymentId: String,
        val transactionId: String,
        val amount: Double,
        val currency: String,
        val timestamp: Long
    ) : PaymentResult()
    
    @kotlinx.serialization.Serializable
    data class Failed(
        val errorCode: String,
        val errorMessage: String,
        val retryable: Boolean = false
    ) : PaymentResult()
    
    @kotlinx.serialization.Serializable
    data class Pending(
        val paymentId: String,
        val estimatedCompletionTime: Long?
    ) : PaymentResult()
}

/**
 * Payment record
 */
@kotlinx.serialization.Serializable
data class Payment(
    val id: String,
    val userId: String,
    val amount: Double,
    val currency: String,
    val status: PaymentStatus,
    val paymentMethod: PaymentMethod,
    val timestamp: Long,
    val description: String? = null
)

/**
 * Payment status
 */
@kotlinx.serialization.Serializable
enum class PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED,
    CANCELLED
}

/**
 * Validation result
 */
@kotlinx.serialization.Serializable
data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
)

/**
 * Refund result
 */
@kotlinx.serialization.Serializable
sealed class RefundResult {
    @kotlinx.serialization.Serializable
    data class Success(
        val refundId: String,
        val amount: Double,
        val timestamp: Long
    ) : RefundResult()
    
    @kotlinx.serialization.Serializable
    data class Failed(
        val errorCode: String,
        val errorMessage: String
    ) : RefundResult()
}
