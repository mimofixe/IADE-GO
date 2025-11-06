package com.example.projeto_3o_semestre_pdm.ui.qr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_3o_semestre_pdm.databinding.ActivityQrCodeBinding
import com.example.projeto_3o_semestre_pdm.ui.payment.PaymentSuccessActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.text.SimpleDateFormat
import java.util.*

class QRCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrCodeBinding
    private var orderId: String = ""
    private var totalPrice: Double = 0.0
    private var itemCount: Int = 0
    private var isProcessingPayment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        // Get order details
        totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        itemCount = intent.getIntExtra("itemCount", 0)

        showOrderSummary()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Payment"
    }

    private fun showOrderSummary() {
        // Show order summary first
        binding.layoutOrderSummary.visibility = View.VISIBLE
        binding.layoutQrCode.visibility = View.GONE
        binding.layoutProcessing.visibility = View.GONE

        binding.tvSummaryTotal.text = "€${String.format("%.2f", totalPrice)}"
        binding.tvSummaryItems.text = "$itemCount items"
    }

    private fun setupClickListeners() {
        // Generate QR Code (Simulate Payment)
        binding.btnGenerateQR.setOnClickListener {
            if (!isProcessingPayment) {
                generatePaymentQRCode()
            }
        }

        // Cancel button
        binding.btnCancel.setOnClickListener {
            finish()
        }

        // QR Code acts as "payment confirmation"
        binding.btnConfirmPayment.setOnClickListener {
            if (!isProcessingPayment) {
                processPayment()
            }
        }

        binding.btnCancelPayment.setOnClickListener {
            showOrderSummary()
        }
    }

    private fun generatePaymentQRCode() {
        // Hide summary, show QR code
        binding.layoutOrderSummary.visibility = View.GONE
        binding.layoutQrCode.visibility = View.VISIBLE

        orderId = generateOrderId()

        // Get user info
        val prefs = getSharedPreferences("IADE_GO_PREFS", MODE_PRIVATE)
        val userId = prefs.getString("USER_ID", "GUEST") ?: "GUEST"
        val studentNumber = prefs.getString("STUDENT_NUMBER", "") ?: ""

        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        // Create payment QR code content
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
                "status": "PENDING"
            }
        """.trimIndent()

        try {
            // Generate QR code
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    qrBitmap.setPixel(
                        x, y,
                        if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                    )
                }
            }

            binding.ivQrCode.setImageBitmap(qrBitmap)
            binding.tvOrderNumber.text = "Order #$orderId"
            binding.tvQrTotal.text = "€${String.format("%.2f", totalPrice)}"

            // Show instruction
            binding.tvQrInstruction.text = "This QR code represents your payment.\nClick 'Confirm Payment' to complete."

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error generating payment QR", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processPayment() {
        isProcessingPayment = true

        // Show processing state
        binding.layoutQrCode.visibility = View.GONE
        binding.layoutProcessing.visibility = View.VISIBLE

        binding.tvProcessingMessage.text = "Processing payment..."

        // Simulate payment processing (2 seconds)
        Handler(Looper.getMainLooper()).postDelayed({
            // Simulate successful payment
            completePayment()
        }, 2000)
    }

    private fun completePayment() {
        // Save order to "history" (SharedPreferences for demo)
        saveOrderToHistory()

        // Navigate to success screen
        val intent = Intent(this, PaymentSuccessActivity::class.java)
        intent.putExtra("orderId", orderId)
        intent.putExtra("totalPrice", totalPrice)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun saveOrderToHistory() {
        val prefs = getSharedPreferences("IADE_GO_PREFS", MODE_PRIVATE)
        val orderHistory = prefs.getStringSet("ORDER_HISTORY", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        val timestamp = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val orderRecord = "$orderId|€${String.format("%.2f", totalPrice)}|$itemCount items|$timestamp"

        orderHistory.add(orderRecord)

        prefs.edit().putStringSet("ORDER_HISTORY", orderHistory).apply()
    }

    private fun generateOrderId(): String {
        val timestamp = System.currentTimeMillis().toString().takeLast(8)
        val random = ('A'..'Z').shuffled().take(4).joinToString("")
        return "ORD$timestamp$random"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}