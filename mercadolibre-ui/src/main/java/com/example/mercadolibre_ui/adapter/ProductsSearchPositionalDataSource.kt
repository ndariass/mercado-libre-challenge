package com.example.mercadolibre_ui.adapter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.example.mercadolibre_domain.repository.ProductsRepository
import com.example.mercadolibre_ui.manager.ProductsUiManager
import com.example.mercadolibre_ui.model.UiProduct
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [PositionalDataSource] implementation to provide product search results for a paginated view
 *
 * @author Nicolás Arias
 */
@Singleton
class ProductsSearchPositionalDataSource @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val productsUiManager: ProductsUiManager
) : PositionalDataSource<UiProduct>() {

    var searchQuery: String? = null
    var initialLoadErrorLiveData: MutableLiveData<String>? = null
    var rangeLoadErrorLiveData: MutableLiveData<String?>? = null

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<UiProduct>) {
        if (searchQuery.isNullOrBlank()) {
            Log.w(javaClass.name, "Search query cannot be null or empty")
            return
        }

        val response = productsRepository.searchProducts(
            searchQuery!!,
            params.loadSize,
            params.startPosition
        )

        if (response.successful && response.payload != null) {
            callback.onResult(response.payload!!.map(productsUiManager::buildUiProduct))
        } else {
            rangeLoadErrorLiveData?.postValue(productsUiManager.getRangeLoadError(response.error))
            Log.d(
                javaClass.name,
                response.errorMessage ?: "There was an error loading next items for $searchQuery"
            )
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<UiProduct>) {
        if (searchQuery.isNullOrBlank()) {
            Log.w(javaClass.name, "Search query cannot be null or empty")
            return
        }

        //TODO: remove
        productsUiManager.number = 0
        val response = productsRepository.searchProducts(searchQuery!!, params.pageSize, 0)

        if (response.successful && response.payload != null && response.totalElements != null) {
            callback.onResult(
                response.payload!!.map(productsUiManager::buildUiProduct),
                0,
                response.totalElements!!
            )
        } else {
            initialLoadErrorLiveData?.postValue(productsUiManager.getInitialLoadError(response.error))
            Log.d(
                javaClass.name,
                response.errorMessage ?: "There was an error searching $searchQuery"
            )
        }
    }
}
