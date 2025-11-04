package com.example.projeto_3o_semestre_pdm.data.model

import java.util.Date
import java.util.UUID

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val items: List<MenuItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val qrCode: String = "",
    val timestamp: Date = Date(),
    val status: OrderStatus = OrderStatus.PENDING
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    READY,
    COMPLETED,
    CANCELLED
}