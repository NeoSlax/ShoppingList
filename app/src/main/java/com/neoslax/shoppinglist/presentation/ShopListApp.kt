package com.neoslax.shoppinglist.presentation

import android.app.Application
import com.neoslax.shoppinglist.di.DaggerApplicationComponent

class ShopListApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}