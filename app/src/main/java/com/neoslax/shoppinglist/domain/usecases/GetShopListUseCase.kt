package com.neoslax.shoppinglist.domain.usecases

import androidx.lifecycle.LiveData
import com.neoslax.shoppinglist.domain.entities.ShopItem
import com.neoslax.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class GetShopListUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {
    operator fun invoke(): LiveData<List<ShopItem>> = shopListRepository.getShopList()
}