package com.example.projeto_3o_semestre_pdm.ui.navigation

sealed class Screen(val route: String) {
    object MainMenu : Screen("main_menu")
    object Login : Screen("login")
    object MenuSelection : Screen("menu_selection")
    object FoodMenu : Screen("food_menu")
    object Cart : Screen("cart")
    object OrderHistory : Screen("order_history")
    object QRCode : Screen("qr_code/{totalPrice}/{itemCount}") {
        fun createRoute(totalPrice: Double, itemCount: Int) =
            "qr_code/$totalPrice/$itemCount"
    }
    object Map : Screen("map")
    object RequestInfo : Screen("request_info")
    object PaymentSuccess : Screen("payment_success/{orderId}/{totalPrice}") {
        fun createRoute(orderId: String, totalPrice: Double) =
            "payment_success/$orderId/$totalPrice"
    }
}