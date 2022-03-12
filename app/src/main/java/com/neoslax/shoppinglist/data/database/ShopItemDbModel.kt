package com.neoslax.shoppinglist.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_list")
data class ShopItemDbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
)
