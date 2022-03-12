package com.neoslax.shoppinglist.domain

class GetShopItemByIdUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun getShopItem(id: Int): ShopItem{
        return shopListRepository.getShopItem(id)

    }
}