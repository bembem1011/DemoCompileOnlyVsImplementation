package com.tymex.kmp.demo.core.api

import kotlinx.coroutines.flow.Flow

/**
 * User repository interface - designed for compileOnly usage
 * 
 * This interface defines the contract for user data operations.
 * Implementations will be provided by core-impl module.
 * 
 * When used with compileOnly:
 * - Available during compilation for type checking
 * - Not included in final binary
 * - Implementation must be provided at runtime
 */
interface UserRepository {
    
    /**
     * Get user by ID
     * @param userId User identifier
     * @return User data or null if not found
     */
    suspend fun getUserById(userId: String): User?
    
    /**
     * Get all users
     * @return Flow of user list
     */
    fun getAllUsers(): Flow<List<User>>
    
    /**
     * Save user data
     * @param user User to save
     * @return Success status
     */
    suspend fun saveUser(user: User): Result<Unit>
    
    /**
     * Delete user
     * @param userId User ID to delete
     * @return Success status
     */
    suspend fun deleteUser(userId: String): Result<Unit>
    
    /**
     * Search users by criteria
     * @param query Search query
     * @return Matching users
     */
    suspend fun searchUsers(query: String): List<User>
}

/**
 * User data class - part of API contract
 */
@kotlinx.serialization.Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatar: String? = null,
    val isActive: Boolean = true,
    val metadata: Map<String, String> = emptyMap()
)

/**
 * Result wrapper for operations
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    
    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
    }
    
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    inline fun onError(action: (Throwable) -> Unit): Result<T> {
        if (this is Error) action(exception)
        return this
    }
}
