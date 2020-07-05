package com.example.mercadolibre_data.di

import com.example.mercadolibre_data.network.ProductsRestApi
import com.example.mercadolibre_data.repository.RestApiProductRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

/**
 * Dagger module class for the data layer
 *
 * @author Nicolás Arias
 */
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideProductRepository(restApiProductRepository: RestApiProductRepository) =
        restApiProductRepository

    @Provides
    @Singleton
    fun provideProductRestApi(): ProductsRestApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        return retrofit.create(ProductsRestApi::class.java)
    }
}
