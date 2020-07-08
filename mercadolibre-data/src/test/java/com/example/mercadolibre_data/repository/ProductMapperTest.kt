package com.example.mercadolibre_data.repository

import com.example.mercadolibre_data.TestUtils.readFromFile
import com.example.mercadolibre_data.TestUtils.readFromJson
import com.example.mercadolibre_data.dto.ProductDto
import com.example.mercadolibre_data.dto.ProductResponseDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [ProductMapper]
 *
 * @author Nicolás Arias
 */
class ProductMapperTest {

    private val subject =
        ProductMapper()

    @Test
    fun test_map_Given_AllFieldsPresent_Then_ReturnNonNullModel() {
        val resultDto = readFromFile(
            path = "/products.json",
            clazz = ProductResponseDto::class.java
        )

        assertEquals(1, resultDto.results!!.size)
        val productDto = resultDto.results!![0]!!

        subject.map(productDto)!!.apply {
            assertEquals(productDto.id, id)
            assertEquals(productDto.title, title)
            assertEquals(productDto.price, price)
            assertEquals(productDto.currencyId, currencyId)
            assertEquals(productDto.availableQuantity, availableQuantity)
            assertEquals(productDto.soldQuantity, soldQuantity)
            assertEquals(productDto.condition, condition!!.value)
            assertEquals(productDto.thumbnail, thumbnail)

            assertEquals(productDto.installments!!.quantity, installments!!.quantity)
            assertEquals(productDto.installments!!.amount, installments!!.amount)
            assertEquals(productDto.installments!!.currencyId, installments!!.currencyId)

            assertEquals(
                "${productDto.address!!.stateName}, ${productDto.address!!.cityName}",
                address
            )

            assertEquals(productDto.shipping!!.freeShipping, shipping.freeShipping)
            assertEquals(productDto.shipping!!.storePickUp, shipping.storePickUp)

            assertEquals(productDto.attributes!!.size, attributes.size)

            productDto.attributes!!.zip(attributes).forEach {
                assertEquals(it.first!!.name, it.second.name)
                assertEquals(it.first!!.valueName, it.second.valueName)
            }

            assertEquals(productDto.originalPrice, originalPrice)
        }
    }

    @Test
    fun test_map_Given_OnlyMandatoryFieldsPresent_Then_ReturnNonNullModel() {
        val resultDto = readFromFile(
            path = "/products-only-mandatory-fields.json",
            clazz = ProductResponseDto::class.java
        )

        assertEquals(1, resultDto.results!!.size)
        val productDto = resultDto.results!![0]!!

        subject.map(productDto)!!.apply {
            assertEquals(productDto.id, id)
            assertEquals(productDto.title, title)
            assertEquals(productDto.price, price)
            assertEquals(productDto.currencyId, currencyId)
            assertNull(availableQuantity)
            assertNull(soldQuantity)
            assertNull(condition)
            assertNull(thumbnail)
            assertNull(installments)
            assertNull(address)
            assertFalse(shipping.freeShipping)
            assertFalse(shipping.storePickUp)
            assertTrue(attributes.isEmpty())
            assertNull(originalPrice)
        }
    }

    @Test
    fun test_map_Given_MandatoryFieldsMissing_Then_ReturnNullModel() {
        val resultDto = readFromJson(
            json = "{}",
            clazz = ProductDto::class.java
        )

        assertNull(subject.map(resultDto))
    }

    @Test
    fun test_map_Given_OptionalAttributesAreInvalid_Then_ReturnModelWithNullFields() {
        val resultDto = readFromFile(
            path = "/products-wrong-fields.json",
            clazz = ProductResponseDto::class.java
        )

        subject.map(resultDto.results!![0])!!.apply {
            assertNull(condition)
            assertNull(installments)
            assertNull(address)
            assertTrue(attributes.isEmpty())
        }
    }

    @Test
    fun test_map_Given_AddressStateOrCityIsMissingIsNotValid_Then_ReturnAddress() {
        val resultDto = readFromFile(
            path = "/products-missing-address-fields.json",
            clazz = ProductResponseDto::class.java
        )

        val productDto1 = resultDto.results!![0]!!
        val productDto2 = resultDto.results!![0]!!

        assertEquals(productDto1.address!!.cityName, subject.map(productDto1)!!.address)
        assertEquals(productDto2.address!!.cityName, subject.map(productDto2)!!.address)
    }
}
