package com.neoslax.shoppinglist.domain

class GetShopItemByIdUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopItem(id: Int): ShopItem{
        return shopListRepository.getShopItem(id)

    }
}