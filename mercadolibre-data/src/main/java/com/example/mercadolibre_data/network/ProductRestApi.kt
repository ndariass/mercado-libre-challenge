package com.example.mercadolibre_data.network

import com.example.mercadolibre_data.dto.ProductDto
import com.example.mercadolibre_data.dto.ProductResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface to access the Products REST API using Retrofit
 *
 * @author Nicolás Arias
 */
interface ProductsRestApi {

    companion object {
        const val DEFAULT_SITE = "MCO"
    }

    @GET("sites/{site}/search")
    fun searchProducts(
        @Query("q") query: String,
        @Path("site") site: String = DEFAULT_SITE
    ): Call<ProductResponseDto?>?
}
