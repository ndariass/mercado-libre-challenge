package com.example.mlchallenge.di

import android.content.Context
import com.example.mercadolibre_data.di.DataModule
import com.example.mercadolibre_ui.di.UiModule
import com.example.mlchallenge.MercadolibreApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Dagger application component
 *
 * @author Nicolás Arias
 */
@Singleton
@Component(
    modules = [
        DataModule::class,
        UiModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface ApplicationComponent {

    fun inject(app: MercadolibreApplication)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun bindApplication(application: MercadolibreApplication): Builder

        @BindsInstance
        fun bindContext(context: Context): Builder
    }
}
