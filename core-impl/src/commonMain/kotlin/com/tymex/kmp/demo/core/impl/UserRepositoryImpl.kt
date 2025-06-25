package com.tymex.kmp.demo.core.impl

import com.tymex.kmp.demo.core.api.User
import com.tymex.kmp.demo.core.api.UserRepository
import com.tymex.kmp.demo.core.api.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

/**
 * User repository implementation - uses 'implementation' dependency
 * 
 * This class provides concrete implementation of UserRepository interface.
 * It includes all necessary dependencies and will be included in final binary.
 * 
 * When used with implementation:
 * - Available during compilation and runtime
 * - Included in final binary with all dependencies
 * - Transitive dependencies are propagated to consumers
 */
class UserRepositoryImpl : UserRepository {
    
    // In-memory storage for demo purposes
    private val users = mutableMapOf<String, User>()
    private val _usersFlow = MutableStateFlow<List<User>>(emptyList())
    
    init {
        // Initialize with sample data
        initializeSampleData()
    }
    
    override suspend fun getUserById(userId: String): User? {
        // Simulate network delay
        delay(100)
        return users[userId]
    }
    
    override fun getAllUsers(): Flow<List<User>> {
        return _usersFlow.asStateFlow()
    }
    
    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            // Simulate network operation
            delay(200)
            
            users[user.id] = user
            updateUsersFlow()
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            delay(150)
            
            if (users.containsKey(userId)) {
                users.remove(userId)
                updateUsersFlow()
                Result.Success(Unit)
            } else {
                Result.Error(IllegalArgumentException("User not found: $userId"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun searchUsers(query: String): List<User> {
        delay(100)
        
        return users.values.filter { user ->
            user.name.contains(query, ignoreCase = true) ||
            user.email.contains(query, ignoreCase = true)
        }
    }
    
    private fun initializeSampleData() {
        val sampleUsers = listOf(
            User(
                id = "1",
                name = "John Doe",
                email = "john.doe@tymex.com",
                avatar = "https://example.com/avatar1.jpg",
                metadata = mapOf("department" to "Engineering", "role" to "Senior Developer")
            ),
            User(
                id = "2",
                name = "Jane Smith",
                email = "jane.smith@tymex.com",
                avatar = "https://example.com/avatar2.jpg",
                metadata = mapOf("department" to "Product", "role" to "Product Manager")
            ),
            User(
                id = "3",
                name = "Bob Johnson",
                email = "bob.johnson@tymex.com",
                isActive = false,
                metadata = mapOf("department" to "QA", "role" to "QA Engineer")
            )
        )
        
        sampleUsers.forEach { user ->
            users[user.id] = user
        }
        updateUsersFlow()
    }
    
    private fun updateUsersFlow() {
        _usersFlow.value = users.values.toList()
    }
}

/**
 * Factory for creating UserRepository instances
 * This demonstrates dependency injection patterns
 */
object UserRepositoryFactory {
    
    /**
     * Create repository instance
     * In real implementation, this might use DI framework
     */
    fun create(): UserRepository {
        return UserRepositoryImpl()
    }
    
    /**
     * Create repository with custom configuration
     */
    fun create(config: RepositoryConfig): UserRepository {
        // Configuration could include database settings, network settings, etc.
        return UserRepositoryImpl().apply {
            // Apply configuration
        }
    }
}

/**
 * Repository configuration
 */
data class RepositoryConfig(
    val enableCaching: Boolean = true,
    val cacheSize: Int = 100,
    val networkTimeout: Long = 5000,
    val retryAttempts: Int = 3
)
