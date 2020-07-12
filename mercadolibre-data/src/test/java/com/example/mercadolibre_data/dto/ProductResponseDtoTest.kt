package com.example.mercadolibre_data.dto

import com.example.mercadolibre_data.TestUtils.readFromFile
import com.example.mercadolibre_data.TestUtils.readFromJson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test for Product DTO classes
 *
 * @author Nicolás Arias
 */
class ProductResponseDtoTest {

    @Test
    fun test_Deserialize() {
        val resultDto = readFromFile(
            path = "/products.json",
            clazz = ProductResponseDto::class.java
        )

        resultDto.paging!!.run {
            assertEquals(190, total)
            assertEquals(40, offset)
            assertEquals(20, limit)
        }

        assertEquals(1, resultDto.results!!.size)

        resultDto.results!![0]!!.apply {
            assertEquals("MCO481185321", id)
            assertEquals("Motorola Moto G6 Octacore 32gb 3ram Huella 4g Dualcam 2018", title)
            assertEquals(599900f, price)
            assertEquals("COP", currencyId)
            assertEquals(1, availableQuantity)
            assertEquals(0, soldQuantity)
            assertEquals("used", condition)
            assertEquals(
                "http://mco-s2-p.mlstatic.com/736765-MCO31544518191_072019-I.jpg",
                thumbnail
            )
            assertEquals(36, installments!!.quantity)
            assertEquals(16663.89f, installments!!.amount)
            assertEquals("COP", installments!!.currencyId)
            assertEquals("Bogotá D.C.", address!!.stateName)
            assertEquals("Usaquén", address!!.cityName)
            assertTrue(shipping!!.freeShipping!!)
            assertFalse(shipping!!.storePickUp!!)

            assertEquals(5, attributes!!.size)
            assertEquals("Marca", attributes!![0]!!.name)
            assertEquals("Motorola", attributes!![0]!!.valueName)
            assertEquals("Condición del ítem", attributes!![1]!!.name)
            assertEquals("Usado", attributes!![1]!!.valueName)
            assertEquals("Línea", attributes!![2]!!.name)
            assertEquals("Moto", attributes!![2]!!.valueName)
            assertEquals("Modelo", attributes!![3]!!.name)
            assertEquals("G6 Dual SIM", attributes!![3]!!.valueName)
            assertEquals("Modelo del procesador", attributes!![4]!!.name)
            assertEquals("Snapdragon 450", attributes!![4]!!.valueName)

            assertEquals(900000f, originalPrice)
        }
    }

    @Test
    fun test_Deserialize_MissingProductFields() {
        val resultDto = readFromJson(
            json = "{" +
                    "\"results\": [{}]" +
                    "}",
            clazz = ProductResponseDto::class.java
        )
        assertEquals(1, resultDto.results!!.size)

        resultDto.results!![0]!!.apply {
            assertNull(id)
            assertNull(title)
            assertNull(price)
            assertNull(currencyId)
            assertNull(availableQuantity)
            assertNull(soldQuantity)
            assertNull(condition)
            assertNull(thumbnail)
            assertNull(installments)
            assertNull(address)
            assertNull(shipping)
            assertNull(attributes)
            assertNull(originalPrice)
        }
    }

    @Test
    fun test_Deserialize_MissingPagingFields() {
        val resultDto = readFromJson(
            json = "{" +
                    "\"paging\": {}" +
                    "}",
            clazz = ProductResponseDto::class.java
        )

        assertNotNull(resultDto.paging)

        resultDto.paging!!.apply {
            assertNull(total)
            assertNull(offset)
            assertNull(limit)
        }
    }

    @Test
    fun test_Deserialize_MissingRootFields() {
        val resultDto = readFromJson(
            json = "{" +
                    "\"paging\": null," +
                    "\"results\": null" +
                    "}",
            clazz = ProductResponseDto::class.java
        )

        assertNull(resultDto.results)
        assertNull(resultDto.paging)
    }
}
