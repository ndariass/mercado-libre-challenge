package com.example.mercadolibre_ui.adapter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_domain.model.Response
import com.example.mercadolibre_domain.repository.ProductsRepository
import com.example.mercadolibre_ui.manager.ProductsUiManager
import com.example.mercadolibre_ui.model.UiProduct
import javax.inject.Inject
import javax.inject.Singleton

const val PAGE_SIZE = 20

/**
 * [PageKeyedDataSource] implementation to search products using pagination
 *
 * @author Nicolás Arias
 */
@Singleton
class ProductsSearchPagedDataSource @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val productsUiManager: ProductsUiManager
) : PageKeyedDataSource<Int, UiProduct>() {

    var searchQuery: String? = null
    var initialLoadErrorLiveData: MutableLiveData<String>? = null
    var rangeLoadErrorLiveData: MutableLiveData<String?>? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UiProduct>
    ) {
        if (searchQuery.isNullOrBlank()) {
            Log.w(javaClass.name, "Search query cannot be null or empty")
            return
        }

        //TODO: remove
        productsUiManager.number = 0
        val response = productsRepository.searchProducts(searchQuery!!, PAGE_SIZE, 0)

        if (isResponseValid(response)) {
            val uiProducts = response.payload!!.map(productsUiManager::buildUiProduct)

            callback.onResult(
                uiProducts,
                0,
                response.paging!!.totalElements,
                null,
                response.paging!!.pageSize
            )
        } else {
            initialLoadErrorLiveData?.postValue(productsUiManager.getInitialLoadError(response.error))
            Log.d(
                javaClass.name,
                response.errorMessage ?: "There was an error searching $searchQuery"
            )
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UiProduct>) {
        if (searchQuery.isNullOrBlank()) {
            Log.w(javaClass.name, "Search query cannot be null or empty")
            return
        }

        val response = productsRepository.searchProducts(
            searchQuery!!,
            PAGE_SIZE,
            params.key
        )

        if (isResponseValid(response)) {
            val uiProducts = response.payload!!.map(productsUiManager::buildUiProduct)
            callback.onResult(uiProducts, params.key + response.paging!!.pageSize)
        } else {
            rangeLoadErrorLiveData?.postValue(productsUiManager.getRangeLoadError(response.error))
            Log.d(
                javaClass.name,
                response.errorMessage ?: "There was an error loading next items for $searchQuery"
            )
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UiProduct>) {
        // Not implemented
    }

    private fun isResponseValid(response: Response<List<Product>>) =
        response.successful
                && response.payload != null
                && response.paging != null
}
