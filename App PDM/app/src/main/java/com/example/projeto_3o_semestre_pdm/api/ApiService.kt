package com.example.projeto_3o_semestre_pdm.api

import com.example.projeto_3o_semestre_pdm.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Authentication
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    // Menu
    @GET("menu/items")
    suspend fun getMenuItems(): Response<MenuItemsResponse>

    @GET("menu/items/{category}")
    suspend fun getMenuItemsByCategory(
        @Path("category") category: String
    ): Response<MenuItemsResponse>

    // Orders
    @POST("orders/create")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body request: OrderRequest
    ): Response<OrderResponse>

    @GET("orders/{orderId}")
    suspend fun getOrder(
        @Header("Authorization") token: String,
        @Path("orderId") orderId: String
    ): Response<Order>

    @GET("orders/user/{userId}")
    suspend fun getUserOrders(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<List<Order>>

    // Contact
    @POST("contact/submit")
    suspend fun submitContactForm(@Body request: ContactRequest): Response<ContactResponse>

    // User Profile
    @GET("users/{userId}")
    suspend fun getUserProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<User>

    @PUT("users/{userId}")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body user: User
    ): Response<User>
}