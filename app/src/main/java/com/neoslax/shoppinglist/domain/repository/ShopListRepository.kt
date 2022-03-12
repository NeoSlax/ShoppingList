package com.neoslax.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import com.neoslax.shoppinglist.domain.entities.ShopItem

interface ShopListRepository {

    suspend fun addShopItem(item: ShopItem)

    suspend fun deleteShopItem(shopItem: ShopItem)

    suspend fun editShopItem(shopItem: ShopItem)

    suspend fun getShopItem(id: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>
}