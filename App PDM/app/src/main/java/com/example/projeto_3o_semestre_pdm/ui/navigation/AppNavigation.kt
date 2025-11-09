package com.example.projeto_3o_semestre_pdm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projeto_3o_semestre_pdm.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu.route
    ) {
        composable(Screen.MainMenu.route) {
            MainMenuScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.MenuSelection.route) {
            MenuSelectionScreen(navController = navController)
        }

        composable(Screen.FoodMenu.route) {
            FoodMenuScreen(navController = navController)
        }

        composable(Screen.Cart.route) {
            CartScreen(navController = navController)
        }

        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(navController = navController)
        }

        composable(
            route = Screen.QRCode.route,
            arguments = listOf(
                navArgument("totalPrice") { type = NavType.FloatType },
                navArgument("itemCount") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val totalPrice = backStackEntry.arguments?.getFloat("totalPrice")?.toDouble() ?: 0.0
            val itemCount = backStackEntry.arguments?.getInt("itemCount") ?: 0
            QRCodeScreen(
                navController = navController,
                totalPrice = totalPrice,
                itemCount = itemCount
            )
        }

        composable(Screen.Map.route) {
            MapScreen(navController = navController)
        }

        composable(Screen.RequestInfo.route) {
            RequestInfoScreen(navController = navController)
        }

        composable(
            route = Screen.PaymentSuccess.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("totalPrice") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val totalPrice = backStackEntry.arguments?.getFloat("totalPrice")?.toDouble() ?: 0.0
            PaymentSuccessScreen(
                navController = navController,
                orderId = orderId,
                totalPrice = totalPrice
            )
        }
    }
}