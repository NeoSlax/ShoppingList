package com.neoslax.shoppinglist.domain

interface ShopListRepository {

    fun addShopItem(item: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItem(id: Int): ShopItem

    fun getShopList(): List<ShopItem>
}