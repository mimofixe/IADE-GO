package com.example.projeto_3o_semestre_pdm.ui.contact

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto_3o_semestre_pdm.api.RetrofitClient
import com.example.projeto_3o_semestre_pdm.data.repository.ApiRepository
import com.example.projeto_3o_semestre_pdm.databinding.ActivityRequestInfoBinding
import com.example.projeto_3o_semestre_pdm.ui.common.SuccessActivity
import kotlinx.coroutines.launch

class RequestInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestInfoBinding
    private lateinit var repository: ApiRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = ApiRepository(RetrofitClient.apiService)


        setupClickListeners()
    }



    private fun setupClickListeners() {
        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                submitForm()
            }
        }
    }

    private fun submitForm() {
        val name = binding.etName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val email = binding.etEmail.text.toString()
        val message = binding.etMessage.text.toString()

        showLoading(true)

        lifecycleScope.launch {
            val result = repository.submitContactForm(name, lastName, email, message)

            showLoading(false)

            result.onSuccess { response ->
                Toast.makeText(
                    this@RequestInfoActivity,
                    response.message,
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(Intent(this@RequestInfoActivity, SuccessActivity::class.java))
                finish()
            }

            result.onFailure { error ->
                Toast.makeText(
                    this@RequestInfoActivity,
                    "Error: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnSubmit.isEnabled = !show
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.etName.text.isNullOrEmpty()) {
            binding.etName.error = "This field cannot be empty"
            isValid = false
        }

        if (binding.etLastName.text.isNullOrEmpty()) {
            binding.etLastName.error = "This field cannot be empty"
            isValid = false
        }

        if (binding.etEmail.text.isNullOrEmpty()) {
            binding.etEmail.error = "This field cannot be empty"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
            binding.etEmail.error = "Please enter a valid email"
            isValid = false
        }

        if (binding.etMessage.text.isNullOrEmpty()) {
            binding.etMessage.error = "This field cannot be empty"
            isValid = false
        }

        return isValid
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}