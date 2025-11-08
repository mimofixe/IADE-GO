package com.example.projeto_3o_semestre_pdm.data.model

import com.google.gson.annotations.SerializedName

// Login Request
data class LoginRequest(
    @SerializedName("student_number")
    val studentNumber: String,
    @SerializedName("password")
    val password: String
)

// Login Response
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User? = null,
    @SerializedName("token")
    val token: String? = null
)

// Contact Request
data class ContactRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("message")
    val message: String
)

// Contact Response
data class ContactResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
)

// Menu Items Response
data class MenuItemsResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("items")
    val items: List<MenuItemAPI>
)

// Menu Item from API
data class MenuItemAPI(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("category")
    val category: String,
    @SerializedName("image_url")
    val imageUrl: String?
)

// Order Request
data class OrderRequest(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("items")
    val items: List<OrderItemRequest>,
    @SerializedName("total_price")
    val totalPrice: Double
)

data class OrderItemRequest(
    @SerializedName("item_id")
    val itemId: Int,
    @SerializedName("quantity")
    val quantity: Int
)

// Order Response
data class OrderResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("qr_code")
    val qrCode: String
)