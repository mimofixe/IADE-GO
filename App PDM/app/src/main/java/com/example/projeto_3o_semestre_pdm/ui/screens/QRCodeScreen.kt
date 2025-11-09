package com.example.projeto_3o_semestre_pdm.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projeto_3o_semestre_pdm.data.CartManager
import com.example.projeto_3o_semestre_pdm.ui.navigation.Screen
import com.example.projeto_3o_semestre_pdm.ui.theme.AccentTeal
import com.example.projeto_3o_semestre_pdm.ui.theme.PrimaryBlue
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "QRCodeScreen"

enum class PaymentState {
    CHOOSE, PROCESSING, SUCCESS, SHOW_QR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeScreen(
    navController: NavController,
    totalPrice: Double,
    itemCount: Int
) {
    var currentState by remember { mutableStateOf(PaymentState.CHOOSE) }
    var orderId by remember { mutableStateOf("") }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Generate order ID and QR code on screen load
    LaunchedEffect(Unit) {
        try {
            Log.d(TAG, "Starting to generate order ID and QR code")
            orderId = generateOrderId()
            Log.d(TAG, "Order ID generated: $orderId")

            withContext(Dispatchers.IO) {
                qrBitmap = generateQRCode(context, orderId, totalPrice, itemCount)
            }
            Log.d(TAG, "QR Code generated successfully: ${qrBitmap != null}")
        } catch (e: Exception) {
            Log.e(TAG, "Error in LaunchedEffect", e)
            errorMessage = e.message
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    if (currentState == PaymentState.CHOOSE) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Show error if any
            errorMessage?.let { error ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error occurred:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { navController.navigateUp() }) {
                        Text("Go Back")
                    }
                }
                return@Scaffold
            }

            when (currentState) {
                PaymentState.CHOOSE -> {
                    ChoosePaymentScreen(
                        totalPrice = totalPrice,
                        itemCount = itemCount,
                        onPayNow = {
                            Log.d(TAG, "Pay Now clicked")
                            scope.launch {
                                try {
                                    Log.d(TAG, "Starting payment process")
                                    currentState = PaymentState.PROCESSING
                                    delay(2000)

                                    Log.d(TAG, "Moving to success state")
                                    currentState = PaymentState.SUCCESS
                                    delay(2000)

                                    Log.d(TAG, "Saving order to history")
                                    saveOrderToHistory(context, orderId, totalPrice, itemCount, isPaid = true)

                                    Log.d(TAG, "Moving to show QR state")
                                    currentState = PaymentState.SHOW_QR
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error in Pay Now flow", e)
                                    errorMessage = "Payment error: ${e.message}"
                                }
                            }
                        },
                        onPayLater = {
                            Log.d(TAG, "Pay Later clicked")
                            scope.launch {
                                try {
                                    saveOrderToHistory(context, orderId, totalPrice, itemCount, isPaid = false)
                                    currentState = PaymentState.SHOW_QR
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error in Pay Later flow", e)
                                    errorMessage = "Error: ${e.message}"
                                }
                            }
                        }
                    )
                }

                PaymentState.PROCESSING -> {
                    Log.d(TAG, "Showing processing screen")
                    ProcessingScreen()
                }

                PaymentState.SUCCESS -> {
                    Log.d(TAG, "Showing success screen")
                    SuccessScreen()
                }

                PaymentState.SHOW_QR -> {
                    Log.d(TAG, "Showing QR code screen")
                    QRCodeDisplayScreen(
                        qrBitmap = qrBitmap,
                        orderId = orderId,
                        totalPrice = totalPrice,
                        onDone = {
                            Log.d(TAG, "Done clicked, navigating to main menu")
                            navController.navigate(Screen.MainMenu.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChoosePaymentScreen(
    totalPrice: Double,
    itemCount: Int,
    onPayNow: () -> Unit,
    onPayLater: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Order Summary",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Items", fontSize = 16.sp)
                    Text(
                        "$itemCount items",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total Amount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "€${String.format("%.2f", totalPrice)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentTeal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Would you like to pay now?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onPayNow,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentTeal
            )
        ) {
            Text("Pay Now", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onPayLater,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Pay Later", fontSize = 16.sp)
        }
    }
}

@Composable
fun ProcessingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = AccentTeal
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Processing payment...",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please wait",
                fontSize = 14.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }
    }
}

@Composable
fun SuccessScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.checkbox_on_background),
                contentDescription = "Success",
                modifier = Modifier.size(120.dp),
                tint = AccentTeal
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Payment Successful!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Generating your receipt...",
                fontSize = 16.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }
    }
}

@Composable
fun QRCodeDisplayScreen(
    qrBitmap: Bitmap?,
    orderId: String,
    totalPrice: Double,
    onDone: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Order Receipt",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Show this QR code at the counter",
            fontSize = 14.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (qrBitmap != null) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(280.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier.size(280.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AccentTeal)
                        Text(
                            text = "Generating QR...",
                            modifier = Modifier.padding(top = 320.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Order #$orderId",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentTeal
                )

                Text(
                    text = "€${String.format("%.2f", totalPrice)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Thank you for your order!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Pick up at the counter when ready",
            fontSize = 14.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentTeal
            )
        ) {
            Text("Done", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Returning to home in 10 seconds...",
            fontSize = 12.sp,
            color = androidx.compose.ui.graphics.Color.LightGray
        )
    }
}

private fun generateOrderId(): String {
    return try {
        val timestamp = System.currentTimeMillis().toString().takeLast(8)
        val random = ('A'..'Z').shuffled().take(4).joinToString("")
        "ORD$timestamp$random"
    } catch (e: Exception) {
        Log.e(TAG, "Error generating order ID", e)
        "ORD00000000XXXX"
    }
}

private fun generateQRCode(
    context: Context,
    orderId: String,
    totalPrice: Double,
    itemCount: Int
): Bitmap? {
    return try {
        Log.d(TAG, "Generating QR code for order: $orderId")
        val prefs = context.getSharedPreferences("IADE_GO_PREFS", Context.MODE_PRIVATE)
        val userId = prefs.getString("USER_ID", "GUEST") ?: "GUEST"
        val studentNumber = prefs.getString("STUDENT_NUMBER", "") ?: ""

        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val qrContent = """
            {
                "type": "IADE_GO_PAYMENT",
                "order_id": "$orderId",
                "user_id": "$userId",
                "student_number": "$studentNumber",
                "total_price": $totalPrice,
                "item_count": $itemCount,
                "timestamp": "$timestamp",
                "payment_method": "QR_CODE",
                "status": "PAID"
            }
        """.trimIndent()

        Log.d(TAG, "QR Content: $qrContent")

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x, y,
                    if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                )
            }
        }

        Log.d(TAG, "QR code bitmap created successfully")
        bitmap
    } catch (e: Exception) {
        Log.e(TAG, "Error generating QR code", e)
        null
    }
}

private fun saveOrderToHistory(
    context: Context,
    orderId: String,
    totalPrice: Double,
    itemCount: Int,
    isPaid: Boolean
) {
    try {
        Log.d(TAG, "Saving order to history: $orderId, isPaid: $isPaid")
        val prefs = context.getSharedPreferences("IADE_GO_PREFS", Context.MODE_PRIVATE)
        val orderHistory = prefs.getStringSet("ORDER_HISTORY", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val paymentStatus = if (isPaid) "PAID" else "PENDING"
        val orderRecord = "$orderId|€${String.format("%.2f", totalPrice)}|$itemCount items|$timestamp|$paymentStatus"

        orderHistory.add(orderRecord)
        prefs.edit().putStringSet("ORDER_HISTORY", orderHistory).apply()
        Log.d(TAG, "Order saved successfully")

        // Clear cart after saving order
        CartManager.clearCart()
        Log.d(TAG, "Cart cleared")
    } catch (e: Exception) {
        Log.e(TAG, "Error saving order to history", e)
    }
}
