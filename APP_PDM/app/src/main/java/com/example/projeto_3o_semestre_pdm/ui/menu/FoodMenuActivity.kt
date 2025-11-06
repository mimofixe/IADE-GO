package com.example.projeto_3o_semestre_pdm.ui.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projeto_3o_semestre_pdm.R
import com.example.projeto_3o_semestre_pdm.data.model.MenuCategory
import com.example.projeto_3o_semestre_pdm.data.model.MenuItem
import com.example.projeto_3o_semestre_pdm.databinding.ActivityFoodMenuBinding
import com.example.projeto_3o_semestre_pdm.ui.qr.QRCodeActivity

class FoodMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodMenuBinding
    private val selectedItems = mutableListOf<MenuItem>()
    private lateinit var adapter: FoodMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.cantina_bar)
    }

    private fun setupRecyclerView() {
        val menuItems = getMenuItems()

        adapter = FoodMenuAdapter(menuItems) { item ->
            toggleItemSelection(item)
        }

        binding.rvFoodMenu.apply {
            layoutManager = GridLayoutManager(this@FoodMenuActivity, 2)
            adapter = this@FoodMenuActivity.adapter
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
                val intent = Intent(this, QRCodeActivity::class.java)
                intent.putExtra("totalPrice", selectedItems.sumOf { it.price })
                startActivity(intent)
            }
        }
    }

    private fun getMenuItems(): List<MenuItem> {
        return listOf(
            MenuItem(1, R.string.menu_cookies, R.drawable.ic_launcher_background, 1.50, MenuCategory.SNACKS),
            MenuItem(2, R.string.menu_croissant, R.drawable.ic_launcher_background, 1.80, MenuCategory.SNACKS),
            MenuItem(3, R.string.menu_tosta_mista, R.drawable.ic_launcher_background, 2.50, MenuCategory.MEALS),
            MenuItem(4, R.string.menu_brownie, R.drawable.ic_launcher_background, 2.00, MenuCategory.SNACKS),
            MenuItem(5, R.string.menu_sandes_mista, R.drawable.ic_launcher_background, 3.00, MenuCategory.MEALS),
            MenuItem(6, R.string.menu_torradas, R.drawable.ic_launcher_background, 1.50, MenuCategory.SNACKS)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}