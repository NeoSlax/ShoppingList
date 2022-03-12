package com.neoslax.shoppinglist.domain.usecases

import com.neoslax.shoppinglist.domain.entities.ShopItem
import com.neoslax.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class DeleteShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {
    suspend operator fun invoke(shopItem: ShopItem) = shopListRepository.deleteShopItem(shopItem)
}