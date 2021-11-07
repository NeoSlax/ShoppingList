package com.neoslax.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_list")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShopItem(shopItem: ShopItemDbModel)

    @Query(" DELETE FROM shop_list WHERE id=:shopItemId")
    fun deleteShopItem(shopItemId: Int)

    @Query("SELECT * FROM shop_list WHERE id=:shopItemId LIMIT 1")
    fun getShopItem(shopItemId: Int): ShopItemDbModel
}