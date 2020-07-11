package com.example.mercadolibre_data.repository

import com.example.mercadolibre_data.dto.PagingDto
import com.example.mercadolibre_data.dto.ProductDto
import com.example.mercadolibre_data.dto.ProductResponseDto
import com.example.mercadolibre_data.network.ProductsRestApi
import com.example.mercadolibre_data.repository.RestApiProductsRepository.Companion.UNKNOWN_CAUSE
import com.example.mercadolibre_domain.model.Error
import com.example.mercadolibre_domain.model.Product
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

private const val TOTAL_ELEMENTS = 100
private const val PAGE_SIZE = 20
private const val OFFSET = 40

/**
 * Unit tests for [RestApiProductsRepository]
 *
 * @author Nicolás Arias
 */
class RestApiProductsRepositoryTest {

    @InjectMocks
    private lateinit var subject: RestApiProductsRepository

    @Mock
    private lateinit var restApi: ProductsRestApi

    @Mock
    private lateinit var mapper: ProductMapper

    @Mock
    private lateinit var call: Call<*>

    @Mock
    private lateinit var apiResponse: Response<*>

    @Mock
    private lateinit var productDto: ProductDto

    @Mock
    private lateinit var product: Product

    @Mock
    private lateinit var apiErrorResponseBody: ResponseBody

    private val pagingDto = PagingDto(
        total = TOTAL_ELEMENTS,
        limit = PAGE_SIZE,
        offset = OFFSET
    )

    private val pagingDtoWithoutTotal = PagingDto(null, null, null)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        initMocks(this)
    }

    @Test
    fun test_SearchProducts_Given_CallIsSuccessful_And_DtoIsMapped_AndPagingInfoIsPresent_Then_ReturnResultWithPayload() {
        `when`(restApi.searchProducts(query = "query", pageSize = PAGE_SIZE, offset = OFFSET))
            .thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenReturn(apiResponse as Response<List<ProductDto?>?>?)
        `when`(apiResponse.isSuccessful).thenReturn(true)
        `when`(apiResponse.body()).thenReturn(ProductResponseDto(listOf(productDto), pagingDto))
        `when`(mapper.map(productDto)).thenReturn(product)

        subject.searchProducts("query", PAGE_SIZE, OFFSET).apply {
            assertEquals(1, payload!!.size)
            assertEquals(product, payload!![0])
            assertEquals(TOTAL_ELEMENTS, totalElements)
            assertTrue(successful)
            assertNull(error)
            assertNull(errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_CallIsSuccessful_And_DtoIsMapped_AndPagingInfoIsNotPresent_Then_ReturnResultWithOutPayload() {
        `when`(restApi.searchProducts(query = "query", pageSize = PAGE_SIZE, offset = OFFSET))
            .thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenReturn(apiResponse as Response<List<ProductDto?>?>?)
        `when`(apiResponse.isSuccessful).thenReturn(true)
        `when`(apiResponse.body()).thenReturn(ProductResponseDto(listOf(productDto), pagingDtoWithoutTotal))
        `when`(mapper.map(productDto)).thenReturn(product)

        subject.searchProducts("query", PAGE_SIZE, OFFSET).apply {
            assertNull(payload)
            assertNull(totalElements)
            assertFalse(successful)
            assertEquals(Error.GENERAL_ERROR, error)
        }
    }

    @Test
    fun test_SearchProducts_Given_CallIsSuccessful_And_NoDtoIsMapped_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts(query = "query", pageSize = PAGE_SIZE, offset = OFFSET))
            .thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenReturn(apiResponse as Response<List<ProductDto?>?>?)
        `when`(apiResponse.isSuccessful).thenReturn(true)
        `when`(apiResponse.body()).thenReturn(ProductResponseDto(listOf(productDto), pagingDto))
        `when`(mapper.map(productDto)).thenReturn(null)

        subject.searchProducts("query", PAGE_SIZE, OFFSET).apply {
            assertNull(payload)
            assertNull(totalElements)
            assertFalse(successful)
            assertEquals(Error.NOT_FOUND, error)
            assertEquals("No results found", errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_CallIsSuccessful_And_ResultFieldInResponseIsNull_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts(query = "query", pageSize = PAGE_SIZE, offset = OFFSET)).thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenReturn(apiResponse as Response<List<ProductDto?>?>?)
        `when`(apiResponse.isSuccessful).thenReturn(true)
        `when`(apiResponse.body()).thenReturn(ProductResponseDto(null, null))

        subject.searchProducts("query", PAGE_SIZE, OFFSET).apply {
            assertNull(payload)
            assertNull(totalElements)
            assertFalse(successful)
            assertEquals(Error.GENERAL_ERROR, error)
            assertEquals(UNKNOWN_CAUSE, errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_CallIsSuccessful_And_ResultFieldInResponseIsEmpty_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts(query = "query", pageSize = PAGE_SIZE, offset = OFFSET)).thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenReturn(apiResponse as Response<List<ProductDto?>?>?)
        `when`(apiResponse.isSuccessful).thenReturn(true)
        `when`(apiResponse.body()).thenReturn(ProductResponseDto(emptyList(), pagingDto))

        subject.searchProducts("query", PAGE_SIZE, OFFSET).apply {
            assertNull(payload)
            assertNull(totalElements)
            assertFalse(successful)
            assertEquals(Error.NOT_FOUND, error)
            assertEquals("No results found", errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_RestApiReturnsNullCall_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts(query = "query", pageSize = PAGE_SIZE, offset = OFFSET)).thenReturn(null)

        subject.searchProducts("query", PAGE_SIZE, OFFSET).apply {
            assertNull(payload)
            assertNull(totalElements)
            assertFalse(successful)
            assertEquals(Error.GENERAL_ERROR, error)
            assertEquals(UNKNOWN_CAUSE, errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_RestApiReturnsNotSuccessfulCall_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts(query = "query", pageSize = PAGE_SIZE, offset = OFFSET)).thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenReturn(apiResponse)
        `when`(apiResponse.isSuccessful).thenReturn(false)
        `when`(apiResponse.errorBody()).thenReturn(apiErrorResponseBody)
        `when`(apiErrorResponseBody.string()).thenReturn("{Api Response body}")

        subject.searchProducts("query", PAGE_SIZE, OFFSET).apply {
            assertNull(payload)
            assertNull(totalElements)
            assertFalse(successful)
            assertEquals(Error.GENERAL_ERROR, error)
            assertEquals("{Api Response body}", errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_RestApiThrowsException_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts(query = "query", pageSize = PAGE_SIZE, offset = OFFSET)).thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenThrow(IOException("Exception message"))

        subject.searchProducts("query", PAGE_SIZE, OFFSET).apply {
            assertNull(payload)
            assertNull(totalElements)
            assertFalse(successful)
            assertEquals(Error.NETWORK_ERROR, error)
            assertEquals("Exception message", errorMessage)
        }
    }
}
