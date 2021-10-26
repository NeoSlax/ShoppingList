package com.neoslax.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.neoslax.shoppinglist.R
import com.neoslax.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            val diffCallback = ShopListDiffCallback(shopList, value)
            val difResult = DiffUtil.calculateDiff(diffCallback)
            difResult.dispatchUpdatesTo(this)
            field = value
        }

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

        val view = LayoutInflater.from(parent.context).inflate(
            resourceLayout,
            parent,
            false
        )
        return ShopListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val shopItem = shopList[position]
        with(holder) {
            tvName.text = "${shopItem.name} type: ${shopItem.enabled}"
            tvCount.text = shopItem.count.toString()
            itemView.setOnLongClickListener {
                onItemLongClickListener?.invoke(shopItem)
                true
            }
            itemView.setOnClickListener {
                onItemClickListener?.invoke(shopItem)
            }
        }


    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (shopList[position].enabled) {
            SHOP_LIST_ENABLED
        } else {
            SHOP_LIST_DISABLED
        }
    }

    class ShopListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
        val tvCount = itemView.findViewById<TextView>(R.id.tvCount)
    }

    companion object {
        const val SHOP_LIST_DISABLED = 0
        const val SHOP_LIST_ENABLED = 1

        const val MAX_POOL_SIZE = 16
    }

}