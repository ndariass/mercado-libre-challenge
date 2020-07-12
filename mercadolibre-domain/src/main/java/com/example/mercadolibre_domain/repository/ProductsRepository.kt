package com.example.mercadolibre_domain.repository

import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_domain.model.Response

/**
 * Repository for products
 *
 * @author Nicolás Arias
 */
interface ProductsRepository {

    /**
     * Search products matching the given query, limited by the given page size and starting at
     * the given offset
     *
     * @param query the search query to find matching products
     * @param pageSize the maximum number of items to retrieve
     * @param offset the start position to get the items
     * @return a [Response] instance containing the search result
     */
    fun searchProducts(query: String, pageSize: Int, offset: Int): Response<List<Product>>
}
