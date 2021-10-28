package com.neoslax.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.neoslax.shoppinglist.domain.ShopItem
import com.neoslax.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException
import java.util.*

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = sortedSetOf<ShopItem> ({o1, o2 -> o1.id.compareTo(o2.id)})
    private val liveDataShopList = MutableLiveData<List<ShopItem>>()

    private var autoId = 0

    init {
        for (i in 0..10){
            addShopItem(ShopItem("Test name $i", i, Random().nextBoolean()))
        }
    }

    override fun addShopItem(item: ShopItem) {
        if (item.id == ShopItem.UNDEFINED_ID) {
            item.id = autoId++
        }
        shopList.add(item)
        updateLiveData()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateLiveData()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(id: Int): ShopItem {
        return shopList.find { it.id == id }
            ?: throw RuntimeException("Can't find element with id: $id")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return liveDataShopList
    }

    fun updateLiveData()  {
        liveDataShopList.value = shopList.toList()
    }
}