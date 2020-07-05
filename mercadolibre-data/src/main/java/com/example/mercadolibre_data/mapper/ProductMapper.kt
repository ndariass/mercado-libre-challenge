package com.example.mercadolibre_data.mapper

import com.example.mercadolibre_data.dto.ProductDto
import com.example.mercadolibre_domain.model.Product
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mapper class to transform a [ProductDto] instance into a [Product] instance
 *
 * @author Nicolás Arias
 */
@Singleton
class ProductMapper @Inject constructor() {

    /**
     * Maps the given instance of [ProductDto] to an instance of [Product]. If the input is null or
     * any of the mandatory fields are null then null is returned
     *
     * @param dto the object to be mapped
     * @return the mapped instance or null if the input cannot be mapped
     */
    fun map(dto: ProductDto?): Product? =
        dto
            ?.takeIf(this::getMandatoryFieldsFilter)
            ?.run {
                Product(
                    id = id!!,
                    title = title!!,
                    price = price!!,
                    currencyId = currencyId!!,
                    availableQuantity = availableQuantity,
                    condition = mapCondition(condition),
                    thumbnail = thumbnail,
                    installments = mapInstallments(installments),
                    address = mapAddress(address),
                    shipping = mapShipping(shipping),
                    attributes = mapAttributes(attributes),
                    originalPrice = originalPrice
                )
            }

    private fun getMandatoryFieldsFilter(dto: ProductDto) =
        listOf(dto.id, dto.title, dto.currencyId).all { !it.isNullOrBlank() }
                && dto.price != null

    private fun mapCondition(condition: String?): Product.Condition? =
        Product.Condition.values().find { it.value == condition }

    private fun mapInstallments(installments: ProductDto.Installments?): Product.Installments? =
        installments
            ?.takeIf {
                listOf(
                    it.quantity,
                    it.amount
                ).all { it != null } && !it.currencyId.isNullOrBlank()
            }
            ?.run {
                Product.Installments(
                    quantity = quantity!!,
                    amount = amount!!,
                    currencyId = currencyId!!
                )
            }

    private fun mapAddress(address: ProductDto.Address?): String? {
        val result = listOf(address?.stateName, address?.cityName)
            .filter { !it.isNullOrBlank() }
            .joinToString(",")

        return if (result.isEmpty()) null
        else result
    }

    private fun mapShipping(shipping: ProductDto.Shipping?): Product.Shipping =
        Product.Shipping(
            freeShipping = shipping?.freeShipping ?: false,
            storePickUp = shipping?.storePickUp ?: false
        )

    private fun mapAttributes(attributes: List<ProductDto.Attribute?>?): List<Product.Attribute> =
        attributes
            ?.filter { !it?.name.isNullOrBlank() && !it?.valueName.isNullOrBlank() }
            ?.map {
                Product.Attribute(
                    name = it!!.name!!,
                    valueName = it.valueName!!
                )
            } ?: emptyList()
}
