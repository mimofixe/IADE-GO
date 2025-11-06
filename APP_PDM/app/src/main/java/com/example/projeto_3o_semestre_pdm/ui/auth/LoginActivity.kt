package com.example.projeto_3o_semestre_pdm.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto_3o_semestre_pdm.api.RetrofitClient
import com.example.projeto_3o_semestre_pdm.data.repository.ApiRepository
import com.example.projeto_3o_semestre_pdm.databinding.ActivityLoginBinding
import com.example.projeto_3o_semestre_pdm.ui.menu.MenuSelectionActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var repository: ApiRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize repository
        repository = ApiRepository(RetrofitClient.apiService)


        setupClickListeners()
    }


    private fun setupClickListeners() {
        binding.btnSubmit.setOnClickListener {
            val studentNumber = binding.etStudentNumber.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInput(studentNumber, password)) {
                performLogin(studentNumber, password)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Password recovery not implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogin(studentNumber: String, password: String) {
        // Show loading
        showLoading(true)

        // Call API using coroutine
        lifecycleScope.launch {
            val result = repository.login(studentNumber, password)

            // Hide loading
            showLoading(false)

            result.onSuccess { response ->
                // Login successful
                Toast.makeText(
                    this@LoginActivity,
                    "Welcome ${response.user?.name}!",
                    Toast.LENGTH_SHORT
                ).show()

                // Save token and user data (you can use SharedPreferences)
                saveUserData(response.user?.id ?: "", response.token ?: "")

                // Navigate to menu
                val intent = Intent(this@LoginActivity, MenuSelectionActivity::class.java)
                intent.putExtra("studentNumber", studentNumber)
                intent.putExtra("userId", response.user?.id)
                intent.putExtra("token", response.token)
                startActivity(intent)
                finish()
            }

            result.onFailure { error ->
                // Login failed
                Toast.makeText(
                    this@LoginActivity,
                    "Login failed: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnSubmit.isEnabled = !show
        binding.etStudentNumber.isEnabled = !show
        binding.etPassword.isEnabled = !show
    }

    private fun saveUserData(userId: String, token: String) {
        val prefs = getSharedPreferences("IADE_GO_PREFS", MODE_PRIVATE)
        prefs.edit().apply {
            putString("USER_ID", userId)
            putString("AUTH_TOKEN", token)
            putBoolean("IS_LOGGED_IN", true)
            apply()
        }
    }

    private fun validateInput(studentNumber: String, password: String): Boolean {
        if (studentNumber.isEmpty()) {
            binding.etStudentNumber.error = "This field cannot be empty"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "This field cannot be empty"
            return false
        }

        return true
    }
}