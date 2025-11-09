package com.example.projeto_3o_semestre_pdm.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.projeto_3o_semestre_pdm.ui.theme.AccentTeal
import com.example.projeto_3o_semestre_pdm.ui.theme.PrimaryBlue
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.text.SimpleDateFormat
import java.util.*

data class Order(
    val orderId: String,
    val totalPrice: String,
    val items: String,
    val timestamp: String,
    val status: String,
    val itemCount: Int,
    val priceValue: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(navController: NavController) {
    val context = LocalContext.current
    var orders by remember { mutableStateOf(loadOrders(context)) }
    var showClearDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order History") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (orders.isNotEmpty()) {
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(
                                painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_delete),
                                contentDescription = "Clear History",
                                tint = androidx.compose.ui.graphics.Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = androidx.compose.ui.graphics.Color.White,
                    navigationIconContentColor = androidx.compose.ui.graphics.Color.White
                )
            )
        }
    ) { paddingValues ->
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_recent_history),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = androidx.compose.ui.graphics.Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No orders yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your order history will appear here",
                        fontSize = 14.sp,
                        color = androidx.compose.ui.graphics.Color.LightGray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders) { order ->
                    ExpandableOrderCard(order = order, context = context)
                }
            }
        }
    }

    // Clear History Confirmation Dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            icon = {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_dialog_alert),
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color(0xFFFF6B6B)
                )
            },
            title = {
                Text(
                    text = "Clear Order History?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will permanently delete all your order history. This action cannot be undone.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        clearOrderHistory(context)
                        orders = emptyList()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFFF6B6B)
                    )
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ExpandableOrderCard(order: Order, context: Context) {
    var expanded by remember { mutableStateOf(false) }
    val qrBitmap = remember(order.orderId) {
        generateOrderQRCode(context, order.orderId, order.priceValue, order.itemCount)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header - Always Visible
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Order #${order.orderId}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentTeal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = order.timestamp,
                        fontSize = 12.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = AccentTeal
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Summary - Always Visible
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.items,
                        fontSize = 14.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = if (order.status == "PAID") AccentTeal else androidx.compose.ui.graphics.Color.LightGray,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = order.status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            color = androidx.compose.ui.graphics.Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = order.totalPrice,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentTeal
                )
            }

            // Expanded Details
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Order Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Order Information
                OrderDetailRow(label = "Order ID", value = order.orderId)
                OrderDetailRow(label = "Date & Time", value = order.timestamp)
                OrderDetailRow(label = "Items", value = order.items)
                OrderDetailRow(label = "Total", value = order.totalPrice)
                OrderDetailRow(label = "Payment Status", value = order.status)

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // QR Code Section
                Text(
                    text = "Order QR Code",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Show this QR code at the counter",
                    fontSize = 12.sp,
                    color = androidx.compose.ui.graphics.Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (qrBitmap != null) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "Order QR Code",
                                modifier = Modifier.size(200.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier.size(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = AccentTeal)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Scan to verify order",
                            fontSize = 12.sp,
                            color = androidx.compose.ui.graphics.Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

private fun loadOrders(context: android.content.Context): List<Order> {
    val prefs = context.getSharedPreferences("IADE_GO_PREFS", android.content.Context.MODE_PRIVATE)
    val orderHistory = prefs.getStringSet("ORDER_HISTORY", emptySet()) ?: emptySet()

    return orderHistory.mapNotNull { orderString ->
        val parts = orderString.split("|")
        if (parts.size >= 5) {
            try {
                val priceString = parts[1].replace("€", "").replace(",", ".")
                val priceValue = priceString.toDoubleOrNull() ?: 0.0
                val itemCountString = parts[2].split(" ")[0]
                val itemCount = itemCountString.toIntOrNull() ?: 0

                Order(
                    orderId = parts[0],
                    totalPrice = parts[1],
                    items = parts[2],
                    timestamp = parts[3],
                    status = parts[4],
                    itemCount = itemCount,
                    priceValue = priceValue
                )
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }.sortedByDescending { it.timestamp }
}

private fun generateOrderQRCode(
    context: Context,
    orderId: String,
    totalPrice: Double,
    itemCount: Int
): Bitmap? {
    return try {
        val prefs = context.getSharedPreferences("IADE_GO_PREFS", Context.MODE_PRIVATE)
        val userId = prefs.getString("USER_ID", "GUEST") ?: "GUEST"
        val studentNumber = prefs.getString("STUDENT_NUMBER", "") ?: ""
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val qrContent = """{"type":"IADE_GO_PAYMENT","order_id":"$orderId","user_id":"$userId","student_number":"$studentNumber","total_price":$totalPrice,"item_count":$itemCount,"timestamp":"$timestamp"}"""

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 512, 512)
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)

        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}

private fun clearOrderHistory(context: android.content.Context) {
    val prefs = context.getSharedPreferences("IADE_GO_PREFS", android.content.Context.MODE_PRIVATE)
    prefs.edit().remove("ORDER_HISTORY").apply()
}

@Preview(showBackground = true)
@Composable
fun OrderHistoryScreenPreview() {
    com.example.projeto_3o_semestre_pdm.ui.theme.IADEGOTheme {
        OrderHistoryScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableOrderCardPreview() {
    com.example.projeto_3o_semestre_pdm.ui.theme.IADEGOTheme {
        ExpandableOrderCard(
            order = Order(
                orderId = "ORD12345678ABCD",
                totalPrice = "€12.50",
                items = "5 items",
                timestamp = "15/01/2025 14:30",
                status = "PAID",
                itemCount = 5,
                priceValue = 12.50
            ),
            context = LocalContext.current
        )
    }
}