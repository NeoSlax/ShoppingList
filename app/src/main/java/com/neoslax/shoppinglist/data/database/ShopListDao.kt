package com.neoslax.shoppinglist.data.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_list")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Query("SELECT * FROM shop_list")
    fun getShopListCursor(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopItem(shopItem: ShopItemDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShopItemSync(shopItem: ShopItemDbModel)

    @Query(" DELETE FROM shop_list WHERE id=:shopItemId")
    suspend fun deleteShopItem(shopItemId: Int)

    @Query(" DELETE FROM shop_list WHERE id=:shopItemId")
    fun deleteShopItemSync(shopItemId: Int): Int

    @Query("SELECT * FROM shop_list WHERE id=:shopItemId LIMIT 1")
    suspend fun getShopItem(shopItemId: Int): ShopItemDbModel
}