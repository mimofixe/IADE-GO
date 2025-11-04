package com.example.projeto_3o_semestre_pdm.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projeto_3o_semestre_pdm.R
import com.example.projeto_3o_semestre_pdm.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSubmit.setOnClickListener {
            val studentNumber = binding.etStudentNumber.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInput(studentNumber, password)) {
                // Simulate successful login
                findNavController().navigate(R.id.action_login_to_menuSelection)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(context, "Password recovery not implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(studentNumber: String, password: String): Boolean {
        if (studentNumber.isEmpty()) {
            binding.etStudentNumber.error = getString(R.string.error_empty_field)
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = getString(R.string.error_empty_field)
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}