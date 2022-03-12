package com.neoslax.shoppinglist.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.neoslax.shoppinglist.R
import com.neoslax.shoppinglist.databinding.ItemShopDisabledBinding
import com.neoslax.shoppinglist.databinding.ItemShopEnabledBinding
import com.neoslax.shoppinglist.domain.entities.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopListViewHolder>(ShopItemDiffCallback) {


    var onItemLongClickListener: ((shopItem: ShopItem) -> Unit)? = null
    var onItemClickListener: ((shopItem: ShopItem) -> Unit)? = null

    var counter = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {


        Log.d("ShopItemAdapter", "onCreateViewHolder count = ${++counter}")

        val resourceLayout = when (viewType) {
            SHOP_LIST_DISABLED -> R.layout.item_shop_disabled
            SHOP_LIST_ENABLED -> R.layout.item_shop_enabled
            else -> throw RuntimeException("Can't find layout with id $viewType")
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            resourceLayout,
            parent,
            false
        )
        return ShopListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding = holder.binding
        when (binding) {
            is ItemShopEnabledBinding -> {
                binding.shopItem = shopItem
            }
            is ItemShopDisabledBinding -> {
                binding.shopItem = shopItem
            }
        }
        with(binding.root) {
            setOnLongClickListener {
                onItemLongClickListener?.invoke(shopItem)
                true
            }
            setOnClickListener {
                onItemClickListener?.invoke(shopItem)

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enabled) {
            SHOP_LIST_ENABLED
        } else {
            SHOP_LIST_DISABLED
        }
    }

    companion object {
        const val SHOP_LIST_DISABLED = 0
        const val SHOP_LIST_ENABLED = 1

        const val MAX_POOL_SIZE = 16
    }

}