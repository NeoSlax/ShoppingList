package com.neoslax.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.neoslax.shoppinglist.R
import com.neoslax.shoppinglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ShopItemFragment.OnShopItemFragmentExit {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        binding.actionButtonMainActivity.setOnClickListener {
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
        with(binding) {
            rvMain.adapter = adapter
            rvMain.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.SHOP_LIST_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            rvMain.recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.SHOP_LIST_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupOnLongClickListener()
        setupOnItemClickListener()
        setupOnSwipeListener(binding.rvMain)
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

    private fun isOnLandMode(): Boolean = binding.itemContainerView != null

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