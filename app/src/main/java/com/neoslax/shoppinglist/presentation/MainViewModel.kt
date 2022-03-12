package com.neoslax.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neoslax.shoppinglist.domain.usecases.DeleteShopItemUseCase
import com.neoslax.shoppinglist.domain.usecases.EditShopItemUseCase
import com.neoslax.shoppinglist.domain.usecases.GetShopListUseCase
import com.neoslax.shoppinglist.domain.entities.ShopItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getShopListUseCase: GetShopListUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase
) : ViewModel() {

    var shopList = getShopListUseCase()

    fun deleteShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            deleteShopItemUseCase(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem) {

        viewModelScope.launch {
            val (name, count, enabled, id) = shopItem
            editShopItemUseCase(ShopItem(name, count, !enabled, id))
        }
    }

}