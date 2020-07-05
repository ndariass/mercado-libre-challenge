package com.example.mercadolibre_data.mapper

import com.example.mercadolibre_data.TestUtils.readFromFile
import com.example.mercadolibre_data.dto.ProductResponseDto
import org.junit.Test

/**
 * Unit tests for [ProductMapper]
 *
 * @author Nicolás Arias
 */
class ProductMapperTest {

    private val subject = ProductMapper()

    @Test
    fun test_map_Given_AllFieldsPresent_Then_ReturnNonNullModel() {
        val dto = readFromFile(
            path = "/products.json",
            clazz = ProductResponseDto::class.java
        )

        subject.map(dto.results!![0])
    }

    @Test
    fun test_map_Given_OnlyMandatoryFieldsPresent_Then_ReturnNonNullModel() {

    }

    @Test
    fun test_map_Given_MandatoryFieldsMissing_Then_ReturnNullModel() {

    }

    @Test
    fun test_map_Given_ConditionIsValid_Then_ReturnNonNullCondition() {

    }

    @Test
    fun test_map_Given_ConditionIsNotValid_Then_ReturnNullCondition() {

    }

    @Test
    fun test_map_Given_InstallmentsIsValid_Then_ReturnNonNullInstallments() {

    }

    @Test
    fun test_map_Given_InstallmentsIsNotValid_Then_ReturnNullInstallments() {

    }

    @Test
    fun test_map_Given_AddressIsValid_Then_ReturnNonNullAddress() {

    }

    @Test
    fun test_map_Given_AddressIsNotValid_Then_ReturnNullAddress() {

    }

    @Test
    fun test_map_Given_ShippingIsValid_Then_ReturnNonNullShipping() {

    }

    @Test
    fun test_map_Given_ShippingIsNotValid_Then_ReturnDefaultShipping() {

    }

    @Test
    fun test_map_Given_AttributesAreValid_Then_ReturnNonNullAttributes() {

    }

    @Test
    fun test_map_Given_AttributesAreNotValid_Then_ReturnNullAttributes() {

    }
}