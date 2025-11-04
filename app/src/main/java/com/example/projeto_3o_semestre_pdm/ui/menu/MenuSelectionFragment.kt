package com.example.projeto_3o_semestre_pdm.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projeto_3o_semestre_pdm.R
import com.example.projeto_3o_semestre_pdm.databinding.FragmentMenuSelectionBinding

class MenuSelectionFragment : Fragment() {

    private var _binding: FragmentMenuSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnMapIade.setOnClickListener {
            findNavController().navigate(R.id.action_menuSelection_to_map)
        }

        binding.btnCantinaBar.setOnClickListener {
            findNavController().navigate(R.id.action_menuSelection_to_foodMenu)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}