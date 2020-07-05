package com.example.mercadolibre_ui.di

import com.example.mercadolibre_ui.activity.MyTestClass2
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module class for the UI layer
 *
 * @author Nicolás Arias
 */
@Module
class UiModule {

    @Singleton
    @Provides
    fun provideMyTestClass2() = MyTestClass2()
}
