package com.example.mercadolibre_data.repository

import com.example.mercadolibre_data.network.ProductsRestApi
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_domain.model.Response
import com.example.mercadolibre_domain.repository.ProductsRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [ProductsRepository] implementation using a REST API
 *
 * @author Nicolás Arias
 */
@Singleton
class RestApiProductRepository @Inject constructor(
    private val restApi: ProductsRestApi,
    private val mapper: ProductMapper
) :
    ProductsRepository {

    companion object {
        internal const val UNKNOWN_CAUSE = "Unknown cause"
    }

    /**
     * See documentation in parent class
     */
    override fun searchProducts(query: String): Response<List<Product>> {
        return try {
            val apiResponse = restApi.searchProducts(query)?.execute()

            return if (apiResponse?.isSuccessful == true) {
                apiResponse
                    .body()
                    ?.run { results?.mapNotNull(mapper::map) }
                    ?.let { Response(payload = it, successful = true, errorMessage = null) }
                    ?: Response<List<Product>>(
                        payload = null,
                        successful = false,
                        errorMessage = UNKNOWN_CAUSE
                    )
            } else {
                Response(
                    payload = null,
                    successful = false,
                    errorMessage = apiResponse?.message() ?: UNKNOWN_CAUSE
                )
            }
        } catch (e: IOException) {
            Response(payload = null, successful = false, errorMessage = e.message)
        }
    }
}
