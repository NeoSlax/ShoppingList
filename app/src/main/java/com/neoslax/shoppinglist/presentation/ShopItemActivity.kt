package com.neoslax.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.neoslax.shoppinglist.R

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnShopItemFragmentExit {

    private var screenMode = MODE_UNKNOWN
    private var id = DEFAULT_VAL_GET_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()

        if (savedInstanceState == null) {
            val fragment = when (screenMode) {
                MODE_ADD_NEW_ITEM -> ShopItemFragment.getFragmentAddMode()
                MODE_EDIT_ITEM -> ShopItemFragment.getFragmentEditMode(id)
                else -> throw RuntimeException("Fragment with name $screenMode not found")
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.shop_item_container, fragment)
                .commit()
        }

    }

    override fun onShopItemFragmentExit() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("ScreenMode empty")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT_ITEM && mode != MODE_ADD_NEW_ITEM) {
            throw RuntimeException("ScreenMode not found")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT_ITEM) {
            if (!intent.hasExtra(ITEM_ID)) {
                throw RuntimeException("ShopItem ID not found")
            }
            id = intent.getIntExtra(ITEM_ID, DEFAULT_VAL_GET_ID)
        }
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_screen_mode"
        private const val MODE_ADD_NEW_ITEM = "add_new_item"
        private const val MODE_EDIT_ITEM = "edit_item"
        private const val ITEM_ID = "edit_item_id"
        private const val DEFAULT_VAL_GET_ID = -1
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD_NEW_ITEM)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT_ITEM)
            intent.putExtra(ITEM_ID, shopItemId)
            return intent
        }
    }
}