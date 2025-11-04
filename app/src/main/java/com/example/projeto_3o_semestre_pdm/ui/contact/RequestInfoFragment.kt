package com.example.projeto_3o_semestre_pdm.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projeto_3o_semestre_pdm.R
import com.example.projeto_3o_semestre_pdm.databinding.FragmentRequestInfoBinding

class RequestInfoFragment : Fragment() {

    private var _binding: FragmentRequestInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                findNavController().navigate(R.id.action_requestInfo_to_success)
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (binding.etName.text.isNullOrEmpty()) {
            binding.etName.error = getString(R.string.error_empty_field)
            isValid = false
        }

        if (binding.etLastName.text.isNullOrEmpty()) {
            binding.etLastName.error = getString(R.string.error_empty_field)
            isValid = false
        }

        if (binding.etEmail.text.isNullOrEmpty()) {
            binding.etEmail.error = getString(R.string.error_empty_field)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()) {
            binding.etEmail.error = getString(R.string.error_invalid_email)
            isValid = false
        }

        if (binding.etMessage.text.isNullOrEmpty()) {
            binding.etMessage.error = getString(R.string.error_empty_field)
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}