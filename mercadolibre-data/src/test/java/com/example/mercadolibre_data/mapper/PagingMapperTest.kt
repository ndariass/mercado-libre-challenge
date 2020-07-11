package com.example.mercadolibre_data.mapper

import com.example.mercadolibre_data.dto.PagingDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Unit tests for [PagingMapper]
 *
 * @author Nicolás Arias
 */
class PagingMapperTest {

    private val subject = PagingMapper()

    @Test
    fun test_map_AllFieldsPresent() {
        val pagingDto = PagingDto(total = 100, offset = 40, limit = 20)

        subject.map(pagingDto)!!.apply {
            assertEquals(100, totalElements)
            assertEquals(40, offset)
            assertEquals(20, pageSize)
        }
    }

    @Test
    fun test_map_FieldsMissing() {
        val pagingDto1 = PagingDto(total = 100, offset = 40, limit = null)
        val pagingDto2 = PagingDto(total = 100, offset = null, limit = 20)
        val pagingDto3 = PagingDto(total = null, offset = 40, limit = 20)

        assertNull(subject.map(pagingDto1))
        assertNull(subject.map(pagingDto2))
        assertNull(subject.map(pagingDto3))
    }

    @Test
    fun test_map_NullInput() {
        assertNull(subject.map(null))
    }
}