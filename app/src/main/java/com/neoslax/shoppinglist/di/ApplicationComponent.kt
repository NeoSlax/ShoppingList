package com.neoslax.shoppinglist.di

import android.app.Application
import com.neoslax.shoppinglist.presentation.MainActivity
import com.neoslax.shoppinglist.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
        DomainModule::class
    ]
)
@ApplicationScope
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: ShopItemFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            application: Application
        ): ApplicationComponent
    }
}