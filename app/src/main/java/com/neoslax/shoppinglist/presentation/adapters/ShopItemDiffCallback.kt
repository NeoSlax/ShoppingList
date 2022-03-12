package com.neoslax.shoppinglist.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.neoslax.shoppinglist.domain.entities.ShopItem
import javax.inject.Inject

object ShopItemDiffCallback : DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}