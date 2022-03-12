package com.neoslax.shoppinglist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.neoslax.shoppinglist.data.database.ShopListDao
import com.neoslax.shoppinglist.data.mapper.ShopListMapper
import com.neoslax.shoppinglist.domain.entities.ShopItem
import com.neoslax.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    private val shopListDao: ShopListDao,
    private val mapper: ShopListMapper,
) : ShopListRepository {


    override suspend fun addShopItem(item: ShopItem) {
        shopListDao.insertShopItem(mapper.shopItemToShopItemDb(item))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        addShopItem(shopItem)
    }

    override suspend fun getShopItem(id: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(id)
        return mapper.shopItemDbToShopItem(dbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> =
        MediatorLiveData<List<ShopItem>>().apply {
            addSource(shopListDao.getShopList()) {
                value = mapper.shopItemDbListToShopList(it)
            }
        }
}