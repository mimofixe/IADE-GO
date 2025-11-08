package com.example.projeto_3o_semestre_pdm.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.projeto_3o_semestre_pdm.data.model.MenuItem

data class CartItem(val menuItem: MenuItem, var quantity: Int)

object CartManager {
    var cart by mutableStateOf<Map<Int, CartItem>>(emptyMap())
        private set

    val totalItems: Int
        get() = cart.values.sumOf { it.quantity }

    val totalPrice: Double
        get() = cart.values.sumOf { it.menuItem.price * it.quantity }

    fun addItem(item: MenuItem) {
        cart = cart.toMutableMap().apply {
            val current = this[item.id]
            if (current == null) {
                this[item.id] = CartItem(item, 1)
            } else {
                current.quantity++
            }
        }
    }

    fun removeItem(item: MenuItem) {
        cart = cart.toMutableMap().apply {
            val current = this[item.id]
            if (current != null) {
                if (current.quantity > 1) {
                    current.quantity--
                } else {
                    this.remove(item.id)
                }
            }
        }
    }

    fun deleteItem(itemId: Int) {
        cart = cart.toMutableMap().apply {
            this.remove(itemId)
        }
    }

    fun clearCart() {
        cart = emptyMap()
    }

    fun getQuantity(itemId: Int): Int {
        return cart[itemId]?.quantity ?: 0
    }
}