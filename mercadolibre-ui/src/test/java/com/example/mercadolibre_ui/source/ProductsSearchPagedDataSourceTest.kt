package com.example.mercadolibre_ui.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import com.example.mercadolibre_domain.model.Error
import com.example.mercadolibre_domain.model.Paging
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_domain.model.Response
import com.example.mercadolibre_domain.repository.ProductsRepository
import com.example.mercadolibre_ui.manager.ProductsUiManager
import com.example.mercadolibre_ui.model.UiProduct
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner

/**
 * Unit tests for [ProductsSearchPagedDataSource]
 *
 * @author Nicolás Arias
 */
@RunWith(RobolectricTestRunner::class)
class ProductsSearchPagedDataSourceTest {

    private val rangeLoadPageOffset = 60

    private val loadRangeParams = PageKeyedDataSource.LoadParams<Int>(rangeLoadPageOffset, 0)

    @InjectMocks
    private lateinit var subject: ProductsSearchPagedDataSource

    @Mock
    private lateinit var productsRepository: ProductsRepository

    @Mock
    private lateinit var productUiManager: ProductsUiManager

    @Mock
    private lateinit var loadInitialParams: PageKeyedDataSource.LoadInitialParams<Int>

    @Mock
    private lateinit var loadInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, UiProduct>

    @Mock
    private lateinit var loadRangeCallback: PageKeyedDataSource.LoadCallback<Int, UiProduct>

    @Mock
    private lateinit var product: Product

    @Mock
    private lateinit var uiProduct: UiProduct

    @Mock
    private lateinit var loadInitialObserver: Observer<String>

    @Mock
    private lateinit var loadRangeObserver: Observer<String?>

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var successfulResponse: Response<List<Product>>

    private lateinit var failedResponse: Response<List<Product>>

    private val searchQuery = "query"

    private val uiErrorMessage = "UI error message"

    @Before
    fun setUp() {
        initMocks(this)
        initTestResponses()

        subject.initialLoadErrorLiveData = MutableLiveData<String>().apply {
            observeForever(loadInitialObserver)
        }
        subject.rangeLoadErrorLiveData = MutableLiveData<String?>().apply {
            observeForever(loadRangeObserver)
        }
    }

    @Test
    fun test_loadInitial_Given_RepositoryResponseIsSuccessful_Then_NotifyResult() {
        `when`(productsRepository.searchProducts(searchQuery, PAGE_SIZE, 0))
            .thenReturn(successfulResponse)
        `when`(productUiManager.buildUiProduct(product)).thenReturn(uiProduct)

        subject.searchQuery = searchQuery
        subject.loadInitial(loadInitialParams, loadInitialCallback)

        verify(loadInitialCallback).onResult(
            listOf(uiProduct),
            0,
            successfulResponse.paging!!.totalElements,
            null,
            successfulResponse.paging!!.pageSize
        )
        verify(loadInitialObserver, never()).onChanged(anyString())
    }

    @Test
    fun test_loadInitial_Given_RepositoryResponseIsNotSuccessful_Then_NotifyError() {
        `when`(productsRepository.searchProducts(searchQuery, PAGE_SIZE, 0))
            .thenReturn(failedResponse)
        `when`(productUiManager.getInitialLoadError(failedResponse.error)).thenReturn(uiErrorMessage)

        subject.searchQuery = searchQuery
        subject.loadInitial(loadInitialParams, loadInitialCallback)

        verify(loadInitialObserver).onChanged(uiErrorMessage)
        verify(loadInitialCallback, never()).onResult(any(), anyInt(), anyInt(), anyInt(), anyInt())
    }

    @Test
    fun test_loadInitial_Given_SearchQueryIsNotSet_Then_DoNothing() {
        subject.searchQuery = null
        subject.loadInitial(loadInitialParams, loadInitialCallback)

        verify(loadInitialObserver, never()).onChanged(anyString())
        verify(loadInitialCallback, never()).onResult(any(), anyInt(), anyInt(), anyInt(), anyInt())
    }

    @Test
    fun test_loadAfter_Given_RepositoryResponseIsSuccessful_Then_NotifyResult() {
        val nextPageExpectedKey = rangeLoadPageOffset + successfulResponse.paging!!.pageSize

        `when`(productsRepository.searchProducts(searchQuery, PAGE_SIZE, rangeLoadPageOffset))
            .thenReturn(successfulResponse)
        `when`(productUiManager.buildUiProduct(product)).thenReturn(uiProduct)

        subject.searchQuery = searchQuery
        subject.loadAfter(loadRangeParams, loadRangeCallback)

        verify(loadRangeCallback).onResult(listOf(uiProduct), nextPageExpectedKey)
        verify(loadRangeObserver, never()).onChanged(anyString())
    }

    @Test
    fun test_loadAfter_Given_RepositoryResponseIsNotSuccessful_Then_NotifyError() {
        `when`(productsRepository.searchProducts(searchQuery, PAGE_SIZE, rangeLoadPageOffset))
            .thenReturn(failedResponse)
        `when`(productUiManager.getRangeLoadError(failedResponse.error)).thenReturn(uiErrorMessage)

        subject.searchQuery = searchQuery
        subject.loadAfter(loadRangeParams, loadRangeCallback)

        verify(loadRangeObserver).onChanged(uiErrorMessage)
        verify(loadRangeCallback, never()).onResult(any(), anyInt())
    }

    @Test
    fun test_loadAfter_Given_SearchQueryIsNotSet_Then_DoNothing() {
        subject.searchQuery = null
        subject.loadAfter(loadRangeParams, loadRangeCallback)

        verify(loadRangeObserver, never()).onChanged(anyString())
        verify(loadRangeCallback, never()).onResult(any(), anyInt())
    }

    private fun initTestResponses() {
        successfulResponse = Response(
            payload = listOf(product),
            paging = Paging(
                totalElements = 100,
                offset = 40,
                pageSize = 20
            ),
            successful = true,
            error = null,
            errorMessage = null
        )

        failedResponse = Response(
            payload = null,
            paging = null,
            successful = false,
            error = Error.GENERAL_ERROR,
            errorMessage = null
        )
    }
}
