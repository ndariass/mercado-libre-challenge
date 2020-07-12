package com.example.mercadolibre_ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mercadolibre_ui.activity.ProductsSearchActivity
import com.example.mercadolibre_ui.fragment.ProductsSearchFragment
import com.example.mercadolibre_ui.viewmodel.ProductsSearchViewModel
import com.example.mercadolibre_ui.viewmodel.ViewModelFactory
import com.example.mercadolibre_ui.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Dagger module class for the UI layer
 *
 * @author Nicolás Arias
 */
@Module
abstract class UiModule() {

    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductsSearchViewModel::class)
    abstract fun bindOrdersViewModel(productsSearchViewModel: ProductsSearchViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun bindProductsSearchActivity(): ProductsSearchActivity

    @ContributesAndroidInjector
    abstract fun bindProductsSearchFragment(): ProductsSearchFragment
}
