package com.example.projeto_3o_semestre_pdm.ui.payment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_3o_semestre_pdm.MainActivity
import com.example.projeto_3o_semestre_pdm.databinding.ActivityPaymentSuccessBinding

class PaymentSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("orderId") ?: "UNKNOWN"
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)

        binding.tvOrderNumber.text = "Order #$orderId"
        binding.tvAmount.text = "€${String.format("%.2f", totalPrice)}"

        // Auto-navigate back to main menu after 4 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }, 4000)

        binding.btnDone.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}