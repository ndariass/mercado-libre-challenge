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
     * Search all products matching the given query
     *
     * @param query the search query to find matching products
     * @return a [List] containing the matching products
     */
    fun searchProducts(query: String): Response<List<Product>>
}
