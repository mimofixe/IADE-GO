package com.example.projeto_3o_semestre_pdm.ui.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_3o_semestre_pdm.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Mapa IADE"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}