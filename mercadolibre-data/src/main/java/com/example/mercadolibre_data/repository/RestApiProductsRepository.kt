package com.example.mercadolibre_data.repository

import com.example.mercadolibre_data.dto.ProductResponseDto
import com.example.mercadolibre_data.network.ProductsRestApi
import com.example.mercadolibre_domain.model.Error
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
class RestApiProductsRepository @Inject constructor(
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
    override fun searchProducts(
        query: String,
        pageSize: Int,
        offset: Int
    ): Response<List<Product>> {
        return try {
            val apiResponse = restApi.searchProducts(
                query = query,
                pageSize = pageSize,
                offset = offset
            )?.execute()

            return if (apiResponse?.isSuccessful == true) {
                buildResultFromSuccessfulApiResponse(apiResponse)
            } else {
                Response(
                    payload = null,
                    totalElements = null,
                    error = Error.GENERAL_ERROR,
                    successful = false,
                    errorMessage = apiResponse?.errorBody()?.string() ?: UNKNOWN_CAUSE
                )
            }
        } catch (e: IOException) {
            Response(
                payload = null,
                totalElements = null,
                successful = false,
                error = Error.NETWORK_ERROR,
                errorMessage = e.message
            )
        }
    }

    private fun buildResultFromSuccessfulApiResponse(apiResponse: retrofit2.Response<ProductResponseDto?>)
            : Response<List<Product>> {

        val responseBody = apiResponse
            .body()

        val searchResults = responseBody?.results?.mapNotNull(mapper::map)
        val pagingTotalElements = responseBody?.paging?.total

        return if (searchResults != null && pagingTotalElements != null) {
            buildResponseFromNonNullResult(searchResults, pagingTotalElements)
        } else {
            Response<List<Product>>(
                payload = null,
                totalElements = null,
                error = Error.GENERAL_ERROR,
                successful = false,
                errorMessage = UNKNOWN_CAUSE
            )
        }
    }

    private fun buildResponseFromNonNullResult(
        products: List<Product>,
        totalElements: Int
    ): Response<List<Product>> {
        if (products.isEmpty()) {
            return Response(
                payload = null,
                totalElements = null,
                error = Error.NOT_FOUND,
                successful = false,
                errorMessage = "No results found"
            )
        } else {
            return Response(
                payload = products,
                totalElements = totalElements,
                error = null,
                successful = true,
                errorMessage = null
            )
        }
    }
}
