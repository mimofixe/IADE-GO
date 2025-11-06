package com.example.projeto_3o_semestre_pdm.ui.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_3o_semestre_pdm.databinding.ActivityMenuSelectionBinding
import com.example.projeto_3o_semestre_pdm.ui.map.MapActivity

class MenuSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get student info from intent
        val studentNumber = intent.getStringExtra("studentNumber") ?: ""

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnMapIade.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        binding.btnCantinaBar.setOnClickListener {
            startActivity(Intent(this, FoodMenuActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}