package com.example.mercadolibre_ui.source

import androidx.lifecycle.MutableLiveData
import com.example.mercadolibre_domain.repository.ProductsRepository
import com.example.mercadolibre_ui.manager.ProductsUiManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

/**
 * Unit tests for [ProductsSearchPagedDataSourceFactory]
 *
 * @author Nicolás Arias
 */
class ProductsSearchPagedDataSourceFactoryTest {

    private lateinit var subject: ProductsSearchPagedDataSourceFactory

    private lateinit var dataSource: ProductsSearchPagedDataSource

    private val searchQuery = "query"

    @Mock
    private lateinit var productsRepository: ProductsRepository

    @Mock
    private lateinit var productsUiManager: ProductsUiManager

    @Mock
    private lateinit var initialLoadErrorLiveData: MutableLiveData<String>

    @Mock
    private lateinit var rangeLoadErrorLiveData: MutableLiveData<String?>

    @Before
    fun setUp() {
        initMocks(this)
        dataSource =
            ProductsSearchPagedDataSource(
                productsRepository,
                productsUiManager
            )
        subject =
            ProductsSearchPagedDataSourceFactory(
                dataSource
            )

        subject.searchQuery = searchQuery
        subject.initialLoadErrorLiveData = initialLoadErrorLiveData
        subject.rangeLoadErrorLiveData = rangeLoadErrorLiveData

    }

    @Test
    fun test_create() {
        assertEquals(dataSource, subject.create())
        dataSource.also {
            assertEquals(searchQuery, it.searchQuery)
            assertEquals(initialLoadErrorLiveData, it.initialLoadErrorLiveData)
            assertEquals(rangeLoadErrorLiveData, it.rangeLoadErrorLiveData)
        }
    }
}
