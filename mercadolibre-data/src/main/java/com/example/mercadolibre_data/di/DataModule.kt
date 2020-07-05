package com.example.mercadolibre_data.di

import com.example.mercadolibre_data.network.ProductsRestApi
import com.example.mercadolibre_data.repository.RestApiProductsRepository
import com.example.mercadolibre_domain.repository.ProductsRepository
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
    fun provideProductRepository(restApiProductsRepository: RestApiProductsRepository): ProductsRepository =
        restApiProductsRepository

    @Provides
    @Singleton
    fun provideProductRestApi(): ProductsRestApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("api.mercadolibre.com/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        return retrofit.create(ProductsRestApi::class.java)
    }
}
