package com.neoslax.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neoslax.shoppinglist.domain.entities.ShopItem
import com.neoslax.shoppinglist.domain.usecases.AddShopItemUseCase
import com.neoslax.shoppinglist.domain.usecases.EditShopItemUseCase
import com.neoslax.shoppinglist.domain.usecases.GetShopItemByIdUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val addShopItemUseCase: AddShopItemUseCase,
    private val getShopItemByIdUseCase: GetShopItemByIdUseCase,
    private val editShopItemUseCase: EditShopItemUseCase
) : ViewModel() {

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
            viewModelScope.launch {
                addShopItemUseCase(shopItem)
                finishWork()
            }
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val isValid = validateInput(name, count)
        if (isValid) {
            shopItem.value?.let {
                val shopItemCopy = it.copy(name = name, count = count)
                viewModelScope.launch {
                    editShopItemUseCase(shopItemCopy)
                    finishWork()
                }
            }
        }
    }

    fun getShopItemById(id: Int) {
        viewModelScope.launch {
            val shopItem = getShopItemByIdUseCase(id)
            _shopItem.value = shopItem
        }


    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return inputCount?.trim()?.toIntOrNull() ?: 0
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
