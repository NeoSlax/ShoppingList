package com.neoslax.shoppinglist.di

import com.neoslax.shoppinglist.data.repository.ShopListRepositoryImpl
import com.neoslax.shoppinglist.domain.repository.ShopListRepository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    @ApplicationScope
    fun bindRepository(impl: ShopListRepositoryImpl): ShopListRepository
}