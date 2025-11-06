package com.example.projeto_3o_semestre_pdm.api

import com.example.projeto_3o_semestre_pdm.data.model.*
import kotlinx.coroutines.delay
import retrofit2.Response

/**
 * Mock implementation of ApiService for development without backend
 * Backend developer: Replace no RetrofitClient.USE_MOCK_DATA = falso quando a api tiver pronta
 */
class MockApiService : ApiService {

    // Simulate network delay
    private suspend fun simulateNetworkDelay() {
        delay(1000) // 1 second delay
    }

    // Mock user database
    private val mockUsers = mutableMapOf(
        "20240000" to User(
            id = "1",
            name = "Sandro Soares",
            studentNumber = "20240000",
            email = "20240000@iade.pt",
            course = "Engenharia Informática",
            qrCode = "USER_20240000"
        )
    )

    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        simulateNetworkDelay()

        val user = mockUsers[request.studentNumber]

        return if (user != null && request.password == "password123") {
            Response.success(
                LoginResponse(
                    success = true,
                    message = "Login successful",
                    user = user,
                    token = "mock_token_${System.currentTimeMillis()}"
                )
            )
        } else {
            Response.success(
                LoginResponse(
                    success = false,
                    message = "Invalid credentials",
                    user = null,
                    token = null
                )
            )
        }
    }

    override suspend fun logout(token: String): Response<Unit> {
        simulateNetworkDelay()
        return Response.success(Unit)
    }

    override suspend fun getMenuItems(): Response<MenuItemsResponse> {
        simulateNetworkDelay()

        val items = listOf(
            MenuItemAPI(1, "Cookies", 1.50, "SNACKS", null),
            MenuItemAPI(2, "Croissant Brioche", 1.80, "SNACKS", null),
            MenuItemAPI(3, "Tosta Mista", 2.50, "MEALS", null),
            MenuItemAPI(4, "Brownie", 2.00, "SNACKS", null),
            MenuItemAPI(5, "Sandes mista", 3.00, "MEALS", null),
            MenuItemAPI(6, "Torradas", 1.50, "SNACKS", null)
        )

        return Response.success(
            MenuItemsResponse(
                success = true,
                items = items
            )
        )
    }

    override suspend fun getMenuItemsByCategory(category: String): Response<MenuItemsResponse> {
        simulateNetworkDelay()

        val allItems = listOf(
            MenuItemAPI(1, "Cookies", 1.50, "SNACKS", null),
            MenuItemAPI(2, "Croissant Brioche", 1.80, "SNACKS", null),
            MenuItemAPI(3, "Tosta Mista", 2.50, "MEALS", null),
            MenuItemAPI(4, "Brownie", 2.00, "SNACKS", null),
            MenuItemAPI(5, "Sandes mista", 3.00, "MEALS", null),
            MenuItemAPI(6, "Torradas", 1.50, "SNACKS", null)
        )

        val filtered = allItems.filter { it.category == category }

        return Response.success(
            MenuItemsResponse(
                success = true,
                items = filtered
            )
        )
    }

    override suspend fun createOrder(
        token: String,
        request: OrderRequest
    ): Response<OrderResponse> {
        simulateNetworkDelay()

        val orderId = "ORD${System.currentTimeMillis()}"
        val qrCode = "QR_$orderId"

        return Response.success(
            OrderResponse(
                success = true,
                message = "Order created successfully",
                orderId = orderId,
                qrCode = qrCode
            )
        )
    }

    override suspend fun getOrder(token: String, orderId: String): Response<Order> {
        simulateNetworkDelay()

        return Response.success(
            Order(
                id = orderId,
                userId = "1",
                items = emptyList(),
                totalPrice = 0.0,
                qrCode = "QR_$orderId",
                status = OrderStatus.PENDING
            )
        )
    }

    override suspend fun getUserOrders(token: String, userId: String): Response<List<Order>> {
        simulateNetworkDelay()
        return Response.success(emptyList())
    }

    override suspend fun submitContactForm(request: ContactRequest): Response<ContactResponse> {
        simulateNetworkDelay()

        // Simulate successful submission
        println("Mock: Contact form submitted - ${request.name} ${request.lastName}")

        return Response.success(
            ContactResponse(
                success = true,
                message = "Your request has been submitted successfully"
            )
        )
    }

    override suspend fun getUserProfile(token: String, userId: String): Response<User> {
        simulateNetworkDelay()

        val user = mockUsers.values.firstOrNull { it.id == userId }

        return if (user != null) {
            Response.success(user)
        } else {
            Response.error(404, okhttp3.ResponseBody.create(null, "User not found"))
        }
    }

    override suspend fun updateUserProfile(
        token: String,
        userId: String,
        user: User
    ): Response<User> {
        simulateNetworkDelay()

        mockUsers[user.studentNumber] = user
        return Response.success(user)
    }
}