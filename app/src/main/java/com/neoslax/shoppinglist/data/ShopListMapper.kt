package com.neoslax.shoppinglist.data

import com.neoslax.shoppinglist.domain.ShopItem

class ShopListMapper {

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