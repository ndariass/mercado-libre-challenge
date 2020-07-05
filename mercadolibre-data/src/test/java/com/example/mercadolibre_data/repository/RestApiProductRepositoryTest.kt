package com.example.mercadolibre_data.repository

import com.example.mercadolibre_data.dto.ProductDto
import com.example.mercadolibre_data.dto.ProductResponseDto
import com.example.mercadolibre_data.mapper.ProductMapper
import com.example.mercadolibre_data.network.ProductsRestApi
import com.example.mercadolibre_data.repository.RestApiProductRepository.Companion.UNKNOWN_CAUSE
import com.example.mercadolibre_domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

/**
 * Unit tests for [RestApiProductRepository]
 *
 * @author Nicolás Arias
 */
class RestApiProductRepositoryTest {

    @InjectMocks
    private lateinit var subject: RestApiProductRepository

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

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun test_SearchProducts_Given_CallIsSuccessful_And_DtoIsMapped_Then_ReturnResultWithPayload() {
        `when`(restApi.searchProducts("query")).thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenReturn(apiResponse as Response<List<ProductDto?>?>?)
        `when`(apiResponse.isSuccessful).thenReturn(true)
        `when`(apiResponse.body()).thenReturn(ProductResponseDto(listOf(productDto)))
        `when`(mapper.map(productDto)).thenReturn(product)

        subject.searchProducts("query").apply {
            assertEquals(1, payload!!.size)
            assertEquals(product, payload!![0])
            assertTrue(successful)
            assertNull(errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_CallIsSuccessful_And_DtoIsNotMapped_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts("query")).thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenReturn(apiResponse as Response<List<ProductDto?>?>?)
        `when`(apiResponse.isSuccessful).thenReturn(true)
        `when`(apiResponse.body()).thenReturn(ProductResponseDto(listOf(productDto)))
        `when`(mapper.map(productDto)).thenReturn(null)

        subject.searchProducts("query").apply {
            assertTrue(payload!!.isEmpty())
            assertTrue(successful)
            assertNull(errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_RestApiReturnsNullCall_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts("query")).thenReturn(null)

        subject.searchProducts("query").apply {
            assertNull(payload)
            assertFalse(successful)
            assertEquals(UNKNOWN_CAUSE, errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_RestApiReturnsNotSuccessfulCall_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts("query")).thenReturn(call as Call<ProductResponseDto?>?)
        `when`(call.execute()).thenReturn(apiResponse)
        `when`(apiResponse.isSuccessful).thenReturn(false)
        `when`(apiResponse.message()).thenReturn("Api Response message")

        subject.searchProducts("query").apply {
            assertNull(payload)
            assertFalse(successful)
            assertEquals("Api Response message", errorMessage)
        }
    }

    @Test
    fun test_SearchProducts_Given_RestApiThrowsException_Then_ReturnResultWithoutPayload() {
        `when`(restApi.searchProducts("query")).thenReturn(call as Call<ProductResponseDto?>?)
        `when`(restApi.searchProducts("query")!!.execute())
            .thenThrow(IOException("Exception message"))

        subject.searchProducts("query").apply {
            assertNull(payload)
            assertFalse(successful)
            assertEquals("Exception message", errorMessage)
        }
    }
}
