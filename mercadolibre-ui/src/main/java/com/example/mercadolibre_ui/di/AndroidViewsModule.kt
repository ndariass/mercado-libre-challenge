package com.example.mercadolibre_ui.di

import com.example.mercadolibre_ui.activity.ProductsSearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Dagger module to support injection in activities and fragments
 *
 * @author Nicolás Arias
 */
@Module
abstract class AndroidViewsModule {

    @ContributesAndroidInjector
    abstract fun bindProductsSearchActivity(): ProductsSearchActivity
}
