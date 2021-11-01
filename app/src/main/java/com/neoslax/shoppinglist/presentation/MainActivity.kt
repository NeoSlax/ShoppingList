package com.neoslax.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.neoslax.shoppinglist.R

class MainActivity : AppCompatActivity(), ShopItemFragment.OnShopItemFragmentExit {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter
    private lateinit var addButton: FloatingActionButton

    private var fragmentContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentContainer = findViewById(R.id.itemContainerView)
        setupRecyclerView()
        addButton = findViewById(R.id.action_button_main_activity)
        addButton.setOnClickListener {
            if (isOnLandMode()) {
                launchFragment(ShopItemFragment.getFragmentAddMode())
            } else {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            }
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this, {
            Log.d("MAIN_ACTIVITY", "viewModel.shopList: ${it.toString()}")
            adapter.submitList(it)
        })


    }

    override fun onShopItemFragmentExit() {
        supportFragmentManager.popBackStack()
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
                val item = adapter.currentList[viewHolder.adapterPosition]
                Log.d("onSwiped", "item = $item, pos = ${viewHolder.adapterPosition}")
                viewModel.deleteShopItem(item)

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
        adapter.onItemClickListener = {
            Log.d("MainActivity", "Click on $it")

            if (isOnLandMode()) {
               launchFragment(ShopItemFragment.getFragmentEditMode(it.id))
            } else {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            }
        }
    }

    private fun isOnLandMode(): Boolean = fragmentContainer != null

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.itemContainerView,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }

    private fun setupOnLongClickListener() {
        adapter.onItemLongClickListener = {
            Log.d("onLongClick", "$it")
            viewModel.changeEnableState(it)
        }
    }


}