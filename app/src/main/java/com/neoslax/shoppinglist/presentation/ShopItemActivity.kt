package com.neoslax.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.neoslax.shoppinglist.R

class ShopItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        if (intent.hasExtra(MODE_ADD_NEW_ITEM)) {
            Toast.makeText(this, "ADD_NEW_ITEM", Toast.LENGTH_SHORT).show()
        }
        if (intent.hasExtra(MODE_EDIT_ITEM)) {
            Toast.makeText(
                this,
                "EDIT: ${intent.getIntExtra(MODE_EDIT_ITEM, DEFAULT_VAL_GET_EXTRA)}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {

        private const val MODE_ADD_NEW_ITEM = "add_new_item"
        private const val MODE_EDIT_ITEM = "edit_item"
        private const val EXTRA_MODE_ADD_NEW = "edit_item"
        private const val DEFAULT_VAL_GET_EXTRA = 0


        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(MODE_ADD_NEW_ITEM, EXTRA_MODE_ADD_NEW)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(MODE_EDIT_ITEM, shopItemId)
            return intent
        }
    }
}