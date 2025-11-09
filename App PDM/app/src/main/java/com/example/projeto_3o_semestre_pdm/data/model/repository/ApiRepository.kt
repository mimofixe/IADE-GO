package com.example.projeto_3o_semestre_pdm.data.repository

import com.example.projeto_3o_semestre_pdm.api.ApiService
import com.example.projeto_3o_semestre_pdm.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepository(private val apiService: ApiService) {

    /**
     * Login user
     * Backend dev: This calls POST /auth/login
     * Expected response: LoginResponse with user data and auth token
     */
    suspend fun login(studentNumber: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(studentNumber, password)
                val response = apiService.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception(body.message))
                    }
                } else {
                    Result.failure(Exception("Login failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Get menu items
     * Backend dev: This calls GET /menu/items
     * Expected response: MenuItemsResponse with list of items
     */
    suspend fun getMenuItems(): Result<List<MenuItemAPI>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMenuItems()

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.items)
                } else {
                    Result.failure(Exception("Failed to fetch menu items"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Submit contact form
     * Backend dev: This calls POST /contact/submit
     * Expected response: ContactResponse with success status
     */
    suspend fun submitContactForm(
        name: String,
        lastName: String,
        email: String,
        message: String
    ): Result<ContactResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = ContactRequest(name, lastName, email, message)
                val response = apiService.submitContactForm(request)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to submit form"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Create order
     * Backend dev: This calls POST /orders/create
     * Expected response: OrderResponse with order ID and QR code
     */
    suspend fun createOrder(
        token: String,
        userId: String,
        items: List<OrderItemRequest>,
        totalPrice: Double
    ): Result<OrderResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = OrderRequest(userId, items, totalPrice)
                val response = apiService.createOrder("Bearer $token", request)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to create order"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Get user profile
     * Backend dev: This calls GET /users/{userId}
     * Expected response: User object
     */
    suspend fun getUserProfile(token: String, userId: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserProfile("Bearer $token", userId)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to fetch user profile"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}