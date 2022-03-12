package com.neoslax.shoppinglist.di

import android.app.Application
import com.neoslax.shoppinglist.data.database.AppDatabase
import com.neoslax.shoppinglist.data.database.ShopListDao
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    @ApplicationScope
    fun provideShopListDao(application: Application): ShopListDao {
        return AppDatabase.getInstance(application).shopListDao()
    }
}