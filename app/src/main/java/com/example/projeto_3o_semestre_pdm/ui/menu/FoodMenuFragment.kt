package com.example.projeto_3o_semestre_pdm.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projeto_3o_semestre_pdm.R
import com.example.projeto_3o_semestre_pdm.data.model.MenuCategory
import com.example.projeto_3o_semestre_pdm.data.model.MenuItem
import com.example.projeto_3o_semestre_pdm.databinding.FragmentFoodMenuBinding

class FoodMenuFragment : Fragment() {

    private var _binding: FragmentFoodMenuBinding? = null
    private val binding get() = _binding!!

    private val selectedItems = mutableListOf<MenuItem>()
    private lateinit var adapter: FoodMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        val menuItems = getMenuItems()

        adapter = FoodMenuAdapter(menuItems) { item ->
            toggleItemSelection(item)
        }

        binding.rvFoodMenu.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@FoodMenuFragment.adapter
        }
    }

    private fun toggleItemSelection(item: MenuItem) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item)
        } else {
            selectedItems.add(item)
        }
        updateTotal()
    }

    private fun updateTotal() {
        val total = selectedItems.sumOf { it.price }
        binding.tvTotal.text = getString(R.string.total_price, total)
        binding.btnCheckout.isEnabled = selectedItems.isNotEmpty()
    }

    private fun setupClickListeners() {
        binding.btnCheckout.setOnClickListener {
            if (selectedItems.isNotEmpty()) {
                findNavController().navigate(R.id.action_foodMenu_to_qrCode)
            }
        }
    }

    private fun getMenuItems(): List<MenuItem> {
        return listOf(
            MenuItem(1, R.string.menu_cookies, android.R.drawable.ic_menu_gallery, 1.50, MenuCategory.SNACKS),
            MenuItem(2, R.string.menu_croissant, android.R.drawable.ic_menu_gallery, 1.80, MenuCategory.SNACKS),
            MenuItem(3, R.string.menu_tosta_mista, android.R.drawable.ic_menu_gallery, 2.50, MenuCategory.MEALS),
            MenuItem(4, R.string.menu_brownie, android.R.drawable.ic_menu_gallery, 2.00, MenuCategory.SNACKS),
            MenuItem(5, R.string.menu_sandes_mista, android.R.drawable.ic_menu_gallery, 3.00, MenuCategory.MEALS),
            MenuItem(6, R.string.menu_torradas, android.R.drawable.ic_menu_gallery, 1.50, MenuCategory.SNACKS)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}