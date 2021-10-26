package com.neoslax.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.neoslax.shoppinglist.R
import com.neoslax.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList?.observe(this, {
            Log.d("MAIN_ACTIVITY", "viewModel.shopList: ${it.toString()}")
            adapter.shopList = it
        })


    }

    private fun setupRecyclerView() {
        adapter = ShopListAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.rvMain)
        recyclerView.adapter = adapter
        recyclerView.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.SHOP_LIST_ENABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )
        recyclerView.recycledViewPool.setMaxRecycledViews(
            ShopListAdapter.SHOP_LIST_DISABLED,
            ShopListAdapter.MAX_POOL_SIZE
        )
        setupOnLongClickListener()
        setupOnItemClickListener()
        setupOnSwipeListener(recyclerView)
    }

    private fun setupOnSwipeListener(recyclerView: RecyclerView) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteShopItem(adapter.shopList[viewHolder.adapterPosition])
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupOnItemClickListener() {
        adapter.onItemClickListener = { Log.d("MainActivity", "Click on $it") }
    }

    private fun setupOnLongClickListener() {
        adapter.onItemLongClickListener = { viewModel.changeEnableState(it) }
    }


}