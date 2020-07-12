package com.example.mercadolibre_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mercadolibre_ui.source.ProductsSearchPagedDataSourceFactory
import com.example.mercadolibre_ui.model.UiProduct
import javax.inject.Inject
import javax.inject.Singleton

private const val PAGE_SIZE = 20
private const val PREFETCH_DISTANCE = 20

/**
 * [ViewModel] implementation for products search
 *
 * @author Nicolás Arias
 */
@Singleton
class ProductsSearchViewModel @Inject constructor(
    private val dataSourceFactory: ProductsSearchPagedDataSourceFactory
) :
    ViewModel() {

    private lateinit var _productDetailNavigation: MutableLiveData<UiProduct>
    private lateinit var _initialLoadError: MutableLiveData<String>
    private lateinit var _rangeLoadError: MutableLiveData<String?>

    lateinit var productDetailNavigation: LiveData<UiProduct>
    lateinit var initialLoadError: LiveData<String>
    lateinit var rangeLoadError: LiveData<String?>

    fun init() {
        _productDetailNavigation = MutableLiveData()
        _initialLoadError = MutableLiveData()
        _rangeLoadError = MutableLiveData()

        productDetailNavigation = _productDetailNavigation
        initialLoadError = _initialLoadError
        rangeLoadError = _rangeLoadError
    }

    fun searchProducts(query: String): LiveData<PagedList<UiProduct>> {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .setEnablePlaceholders(false)
            .build()

        return LivePagedListBuilder(
            dataSourceFactory.apply {
                searchQuery = query
                initialLoadErrorLiveData = _initialLoadError
                rangeLoadErrorLiveData = _rangeLoadError
            },
            config
        ).build()
    }

    fun navigateToProductDetail(uiProduct: UiProduct) {
        _productDetailNavigation.value = uiProduct
    }
}
