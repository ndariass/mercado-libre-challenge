package com.example.mercadolibre_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mercadolibre_ui.adapter.ProductsSearchDataSourceFactory
import com.example.mercadolibre_ui.manager.ProductsUiManager
import com.example.mercadolibre_ui.model.UiProduct
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton

private const val PAGE_SIZE = 20

/**
 * [ViewModel] implementation for products search
 *
 * @author Nicolás Arias
 */
@Singleton
class ProductsSearchViewModel @Inject constructor(
    private val productsUiManager: ProductsUiManager,
    private val dataSourceFactory: ProductsSearchDataSourceFactory
) :
    ViewModel() {

    private lateinit var _productDetailNavigation: MutableLiveData<UiProduct>
    private lateinit var _error: MutableLiveData<String>

    lateinit var productDetailNavigation: LiveData<UiProduct>
    lateinit var error: LiveData<String>

    private var job: Job? = null

    fun init() {
        _productDetailNavigation = MutableLiveData()
        _error = MutableLiveData()

        productDetailNavigation = _productDetailNavigation
        error = _error
    }

    fun searchProducts(query: String): LiveData<PagedList<UiProduct>> {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .build()

        return LivePagedListBuilder(
            dataSourceFactory.apply {
                searchQuery = query
                errorLiveData = _error
            },
            config
        ).build()
    }

    fun navigateToProductDetail(uiProduct: UiProduct) {
        _productDetailNavigation.value = uiProduct
    }

    override fun onCleared() {
        cancelOngoingJob()
        super.onCleared()
    }

    private fun cancelOngoingJob() {
        job?.apply {
            if (!isCancelled) cancel()
        }
    }
}
