package com.example.projeto_3o_semestre_pdm.data.model

data class MenuItem(
    val id: Int,
    val nameResId: Int,
    val imageResId: Int,
    val price: Double,
    val category: MenuCategory = MenuCategory.SNACKS
)

enum class MenuCategory {
    SNACKS,
    DRINKS,
    MEALS
}