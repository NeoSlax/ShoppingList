package com.neoslax.shoppinglist.data.mapper

import com.neoslax.shoppinglist.data.database.ShopItemDbModel
import com.neoslax.shoppinglist.domain.entities.ShopItem
import javax.inject.Inject

class ShopListMapper @Inject constructor() {

    fun shopItemToShopItemDb(shopItem: ShopItem) = ShopItemDbModel(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enabled = shopItem.enabled
    )

    fun shopItemDbToShopItem(shopItemDb: ShopItemDbModel) = ShopItem(
        id = shopItemDb.id,
        name = shopItemDb.name,
        count = shopItemDb.count,
        enabled = shopItemDb.enabled
    )

    fun shopItemDbListToShopList(shopList: List<ShopItemDbModel>) = shopList.map {
        shopItemDbToShopItem(it)
    }
}