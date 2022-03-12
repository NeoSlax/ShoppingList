package com.neoslax.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.neoslax.shoppinglist.domain.ShopItem
import com.neoslax.shoppinglist.domain.ShopListRepository
import java.util.*

class ShopListRepositoryImpl(
    application: Application
) : ShopListRepository {

    private val shopList = sortedSetOf<ShopItem> ({o1, o2 -> o1.id.compareTo(o2.id)})
    private val liveDataShopList = MutableLiveData<List<ShopItem>>()
    private val mapper = ShopListMapper()
    private val appDatabase: AppDatabase = AppDatabase.getInstance(application)

    private var autoId = 0

    init {
        for (i in 0..10){
            //addShopItem(ShopItem("Test name $i", i, Random().nextBoolean()))
        }
    }

    override suspend fun addShopItem(item: ShopItem) {
        appDatabase.shopListDao().insertShopItem(mapper.shopItemToShopItemDb(item))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        appDatabase.shopListDao().deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        addShopItem(shopItem)
    }

    override suspend fun getShopItem(id: Int): ShopItem {
        val dbModel = appDatabase.shopListDao().getShopItem(id)
        return mapper.shopItemDbToShopItem(dbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> = MediatorLiveData<List<ShopItem>>().apply {
            addSource(appDatabase.shopListDao().getShopList()) {
               value = mapper.shopItemDbListToShopList(it)
            }
        }



}