package com.example.projeto_3o_semestre_pdm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_3o_semestre_pdm.databinding.ActivityMainBinding
import com.example.projeto_3o_semestre_pdm.ui.auth.LoginActivity
import com.example.projeto_3o_semestre_pdm.ui.contact.RequestInfoActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRequestInfo.setOnClickListener {
            startActivity(Intent(this, RequestInfoActivity::class.java))
        }
    }
}