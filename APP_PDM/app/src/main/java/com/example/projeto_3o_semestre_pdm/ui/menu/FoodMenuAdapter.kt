package com.example.projeto_3o_semestre_pdm.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_3o_semestre_pdm.data.model.MenuItem
import com.example.projeto_3o_semestre_pdm.databinding.ItemFoodMenuBinding

class FoodMenuAdapter(
    private val items: List<MenuItem>,
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<FoodMenuAdapter.FoodMenuViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodMenuViewHolder {
        val binding = ItemFoodMenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodMenuViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class FoodMenuViewHolder(
        private val binding: ItemFoodMenuBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MenuItem) {
            binding.apply {
                tvItemName.text = root.context.getString(item.nameResId)
                tvItemPrice.text = "€${String.format("%.2f", item.price)}"
                ivItemImage.setImageResource(item.imageResId)

                val isSelected = selectedItems.contains(item.id)
                cardItem.strokeWidth = if (isSelected) 8 else 0

                cardItem.setOnClickListener {
                    if (isSelected) {
                        selectedItems.remove(item.id)
                    } else {
                        selectedItems.add(item.id)
                    }
                    notifyItemChanged(adapterPosition)
                    onItemClick(item)
                }
            }
        }
    }
}