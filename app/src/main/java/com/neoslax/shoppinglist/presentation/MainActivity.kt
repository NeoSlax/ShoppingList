package com.neoslax.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.neoslax.shoppinglist.R
import com.neoslax.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList?.observe(this, {
            Log.d("MAIN_ACTIVITY", "viewModel.shopList: ${it.toString()}")
        })

        viewModel.changeEnableState(
            ShopItem("Test name 1",
                1,
                true,
                1)
        )

    }
}