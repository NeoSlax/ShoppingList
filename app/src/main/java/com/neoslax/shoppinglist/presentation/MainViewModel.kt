package com.neoslax.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.neoslax.shoppinglist.data.ShopListRepositoryImpl
import com.neoslax.shoppinglist.domain.DeleteShopItemUseCase
import com.neoslax.shoppinglist.domain.EditShopItemUseCase
import com.neoslax.shoppinglist.domain.GetShopListUseCase
import com.neoslax.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val shopListRepositoryImpl = ShopListRepositoryImpl(application) //FIXME

    private val getShopListUseCase = GetShopListUseCase(shopListRepositoryImpl)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(shopListRepositoryImpl)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepositoryImpl)

    var shopList = getShopListUseCase.getShopList()

    private val scope = CoroutineScope(Dispatchers.IO)

    fun deleteShopItem(shopItem: ShopItem) {
        scope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }

    }

    fun changeEnableState(shopItem: ShopItem) {

        scope.launch {
            val (name, count, enabled, id) = shopItem
            editShopItemUseCase.editShopItem(ShopItem(name, count, !enabled, id))
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}