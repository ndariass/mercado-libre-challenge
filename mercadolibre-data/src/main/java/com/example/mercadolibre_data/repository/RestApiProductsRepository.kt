package com.example.mercadolibre_data.repository

import com.example.mercadolibre_data.dto.ProductResponseDto
import com.example.mercadolibre_data.mapper.PagingMapper
import com.example.mercadolibre_data.mapper.ProductMapper
import com.example.mercadolibre_data.network.ProductsRestApi
import com.example.mercadolibre_domain.model.Error
import com.example.mercadolibre_domain.model.Paging
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
    private val productMapper: ProductMapper,
    private val pagingMapper: PagingMapper
) :
    ProductsRepository {

    companion object {
        internal const val GENERAL_ERROR_MESSAGE = "There was an error executing the request"
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
                    paging = null,
                    error = Error.GENERAL_ERROR,
                    successful = false,
                    errorMessage = apiResponse?.errorBody()?.string() ?: GENERAL_ERROR_MESSAGE
                )
            }
        } catch (e: IOException) {
            Response(
                payload = null,
                paging = null,
                successful = false,
                error = Error.NETWORK_ERROR,
                errorMessage = e.message
            )
        }
    }

    private fun buildResultFromSuccessfulApiResponse(apiResponse: retrofit2.Response<ProductResponseDto?>)
            : Response<List<Product>> {

        val responseBody = apiResponse.body()

        val searchResults = responseBody?.results?.mapNotNull(productMapper::map)
        val paging = pagingMapper.map(responseBody?.paging)

        return if (searchResults != null && paging != null) {
            buildResponseFromNonNullResult(searchResults, paging)
        } else {
            Response<List<Product>>(
                payload = null,
                paging = null,
                error = Error.GENERAL_ERROR,
                successful = false,
                errorMessage = GENERAL_ERROR_MESSAGE
            )
        }
    }

    private fun buildResponseFromNonNullResult(
        products: List<Product>,
        paging: Paging
    ): Response<List<Product>> {
        if (products.isEmpty()) {
            return Response(
                payload = null,
                paging = null,
                error = Error.NOT_FOUND,
                successful = false,
                errorMessage = "No results found"
            )
        } else {
            return Response(
                payload = products,
                paging = paging,
                error = null,
                successful = true,
                errorMessage = null
            )
        }
    }
}
