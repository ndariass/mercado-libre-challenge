package com.example.mercadolibre_ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mercadolibre_ui.model.UiProduct
import com.example.mercadolibre_ui.source.ProductsSearchPagedDataSource
import com.example.mercadolibre_ui.source.ProductsSearchPagedDataSourceFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks

/**
 * Unit tests for [ProductsSearchViewModel]
 *
 * @author Nicolás Arias
 */
class ProductsSearchViewModelTest {

    private lateinit var subject: ProductsSearchViewModel

    private lateinit var dataSourceFactory: ProductsSearchPagedDataSourceFactory

    @Mock
    private lateinit var dataSource: ProductsSearchPagedDataSource

    @Mock
    private lateinit var uiProduct: UiProduct

    @Mock
    private lateinit var observer: Observer<UiProduct>

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        initMocks(this)
        dataSourceFactory = ProductsSearchPagedDataSourceFactory(dataSource)
        subject = ProductsSearchViewModel(dataSourceFactory)
        subject.init()
    }

    @Test
    fun test_searchProducts() {
        assertNotNull(subject.searchProducts("query"))
        assertEquals(dataSourceFactory.searchQuery, "query")
        assertNotNull(dataSourceFactory.initialLoadErrorLiveData)
        assertNotNull(dataSourceFactory.rangeLoadErrorLiveData)
    }

    @Test
    fun test_navigateToProductDetail() {
        subject.productDetailNavigation.observeForever(observer)

        subject.navigateToProductDetail(uiProduct)

        verify(observer).onChanged(uiProduct)
    }
}
