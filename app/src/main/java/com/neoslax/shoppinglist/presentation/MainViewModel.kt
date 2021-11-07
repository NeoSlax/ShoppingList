package com.neoslax.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.neoslax.shoppinglist.data.ShopListRepositoryImpl
import com.neoslax.shoppinglist.domain.DeleteShopItemUseCase
import com.neoslax.shoppinglist.domain.EditShopItemUseCase
import com.neoslax.shoppinglist.domain.GetShopListUseCase
import com.neoslax.shoppinglist.domain.ShopItem

class MainViewModel : ViewModel() {

    private val shopListRepositoryImpl = ShopListRepositoryImpl //FIXME

    private val getShopListUseCase = GetShopListUseCase(shopListRepositoryImpl)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(shopListRepositoryImpl)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepositoryImpl)

    var shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val (name, count, enabled, id) = shopItem
        editShopItemUseCase.editShopItem(ShopItem(name, count, !enabled, id))
    }
}