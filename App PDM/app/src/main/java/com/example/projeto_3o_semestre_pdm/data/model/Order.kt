package com.example.projeto_3o_semestre_pdm.data.model

import java.util.Date

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<MenuItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val qrCode: String = "", // QR code for this specific order
    val timestamp: Date = Date(),
    val status: OrderStatus = OrderStatus.PENDING
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}