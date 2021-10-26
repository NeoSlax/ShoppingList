package com.neoslax.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.neoslax.shoppinglist.data.ShopListRepositoryImpl
import com.neoslax.shoppinglist.domain.AddShopItemUseCase
import com.neoslax.shoppinglist.domain.EditShopItemUseCase
import com.neoslax.shoppinglist.domain.GetShopItemByIdUseCase
import com.neoslax.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val getShopItemByIdUseCase = GetShopItemByIdUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isValid = validateInput(name, count)
        if (isValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?, shopItem: ShopItem){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isValid = validateInput(name, count)
        if (isValid) {
            val shopItemCopy = ShopItem(name, count, shopItem.enabled, shopItem.id)
            editShopItemUseCase.editShopItem(shopItemCopy)
        }
    }

    fun getShopItemById(id: Int): ShopItem {
        return getShopItemByIdUseCase.getShopItem(id)
    }

    private fun parseName(inputName: String?): String {
        return inputName?.toString()?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int{
        return inputCount?.let {
            it.toString().trim().toIntOrNull()
        } ?: 0
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            // TODO: show error
            result = false
        }
        if (count <= 0) {
            // TODO: show error
            result = false
        }
        return result
    }
}