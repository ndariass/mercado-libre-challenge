package com.example.mercadolibre_ui.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mercadolibre_ui.model.UiProduct
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [DataSource.Factory] implementation to return an instance of [ProductsSearchPositionalDataSource]
 *
 * @author Nicolás Arias
 */
@Singleton
class ProductsSearchPagedDataSourceFactory @Inject constructor(
    private val dataSource: ProductsSearchPagedDataSource
) : DataSource.Factory<Int, UiProduct>() {

    var searchQuery: String? = null
    var initialLoadErrorLiveData: MutableLiveData<String>? = null
    var rangeLoadErrorLiveData: MutableLiveData<String?>? = null

    override fun create(): DataSource<Int, UiProduct> = dataSource.also {
        it.searchQuery = searchQuery
        it.initialLoadErrorLiveData = initialLoadErrorLiveData
        it.rangeLoadErrorLiveData = rangeLoadErrorLiveData
    }
}
