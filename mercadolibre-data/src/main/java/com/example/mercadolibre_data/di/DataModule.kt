package com.example.mercadolibre_data.di

import com.example.mercadolibre_data.network.ProductsRestApi
import com.example.mercadolibre_data.repository.RestApiProductsRepository
import com.example.mercadolibre_domain.repository.ProductsRepository
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
        val objectMapper = jacksonObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.mercadolibre.com/")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        return retrofit.create(ProductsRestApi::class.java)
    }
}
