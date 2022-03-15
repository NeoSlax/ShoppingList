package com.neoslax.shoppinglist.data.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.neoslax.shoppinglist.data.mapper.ShopListMapper
import com.neoslax.shoppinglist.di.DaggerApplicationComponent
import com.neoslax.shoppinglist.domain.entities.ShopItem
import com.neoslax.shoppinglist.presentation.ShopListApp
import javax.inject.Inject

class ShopListProvider : ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.neoslax.shoppinglist", "shop_list", SHOPPING_LIST_CODE)
        addURI("com.neoslax.shoppinglist", "shop_list/#", SHOPPING_LIST_NUMBER_CODE)
        addURI("com.neoslax.shoppinglist", "shop_list/*", SHOPPING_LIST_STRING_CODE)
    }

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: ShopListMapper

    private val component by lazy {
        (context as ShopListApp).component
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code = uriMatcher.match(uri)
        when (code) {
            SHOPPING_LIST_CODE -> {
                return shopListDao.getShopListCursor()
            }
            SHOPPING_LIST_NUMBER_CODE -> {}
            SHOPPING_LIST_STRING_CODE -> {}
            else -> return null
        }
        Log.d("ShopListProvider", "URI: $uri, code: $code")
        return null
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val code = uriMatcher.match(uri)
        when (code) {
            SHOPPING_LIST_CODE -> {
                if (values == null) return null
                val id = values.getAsInteger("id")
                val name = values.getAsString("name")
                val count = values.getAsInteger("count")
                val enabled = values.getAsBoolean("enabled")
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                shopListDao.insertShopItemSync(mapper.shopItemToShopItemDb(shopItem))
            }
            SHOPPING_LIST_NUMBER_CODE -> {}
            SHOPPING_LIST_STRING_CODE -> {}

        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val code = uriMatcher.match(uri)
        when (code) {
            SHOPPING_LIST_CODE -> {
                return shopListDao.deleteShopItemSync(selectionArgs?.get(0)?.toInt() ?: -1)
            }
            SHOPPING_LIST_NUMBER_CODE -> {}
            SHOPPING_LIST_STRING_CODE -> {}

        }
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val code = uriMatcher.match(uri)
        when (code) {
            SHOPPING_LIST_CODE -> {
                if (values == null) return 0
                val id = values.getAsInteger("id")
                val name = values.getAsString("name")
                val count = values.getAsInteger("count")
                val enabled = values.getAsBoolean("enabled")
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                shopListDao.insertShopItemSync(mapper.shopItemToShopItemDb(shopItem))

            }
            SHOPPING_LIST_NUMBER_CODE -> {}
            SHOPPING_LIST_STRING_CODE -> {}

        }
        return 1
    }

    companion object {
        private const val SHOPPING_LIST_CODE = 100
        private const val SHOPPING_LIST_STRING_CODE = 101
        private const val SHOPPING_LIST_NUMBER_CODE = 102
    }
}