package com.neoslax.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() {
            return _errorInputName
        }

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() {
            return _errorInputCount
        }

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _isFinished = MutableLiveData<Unit>()
    val isFinished: LiveData<Unit>
        get() = _isFinished

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isValid = validateInput(name, count)
        if (isValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isValid = validateInput(name, count)
        if (isValid) {
            shopItem.value?.let {
                val shopItemCopy = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(shopItemCopy)
                finishWork() }

        }
    }

    fun getShopItemById(id: Int) {
        val shopItem = getShopItemByIdUseCase.getShopItem(id)
        _shopItem.value = shopItem
    }

    private fun parseName(inputName: String?): String {
        return inputName?.toString()?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return inputCount?.let {
            it.toString().trim().toIntOrNull()
        } ?: 0
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _isFinished.value = Unit
    }
}
