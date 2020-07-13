package com.example.mercadolibre_data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Data transfer object for the products API response
 *
 * @author Nicolás Arias
 */
data class ProductResponseDto(val results: List<ProductDto?>?, val paging: PagingDto?) :
    Serializable {
    companion object {
        private val serialVersionUID: Long = 98962651L
    }
}

/**
 * Data transfer object for the API response pagination information
 *
 * @author Nicolás Arias
 */
data class PagingDto(
    val total: Int?,
    val offset: Int?,
    val limit: Int?
)

/**
 * Data transfer object for a product
 *
 * @author Nicolás Arias
 */
data class ProductDto(
    val id: String?,
    val title: String?,
    val price: Float?,
    @SerializedName("currency_id")
    val currencyId: String?,
    @SerializedName("available_quantity")
    val availableQuantity: Int?,
    @SerializedName("sold_quantity")
    val soldQuantity: Int?,
    val condition: String?,
    val thumbnail: String?,
    val installments: Installments?,
    val address: Address?,
    val shipping: Shipping?,
    val attributes: List<Attribute?>?,
    @SerializedName("original_price")
    val originalPrice: Float?

) : Serializable {
    companion object {
        private val serialVersionUID: Long = 1326841318L
    }

    data class Installments(
        val quantity: Int?,
        val amount: Float?,
        @SerializedName("currency_id")
        val currencyId: String?
    ) : Serializable {
        companion object {
            private val serialVersionUID: Long = 53453325L
        }
    }

    data class Address(
        @SerializedName("state_name")
        val stateName: String?,
        @SerializedName("city_name")
        val cityName: String?
    ) : Serializable {
        companion object {
            private val serialVersionUID: Long = 16897465L
        }
    }

    data class Shipping(
        @SerializedName("free_shipping")
        val freeShipping: Boolean?,
        @SerializedName("store_pick_up")
        val storePickUp: Boolean?
    ) : Serializable {
        companion object {
            private val serialVersionUID: Long = 6568471L
        }
    }

    data class Attribute(
        val name: String?,
        @SerializedName("value_name")
        val valueName: String?
    ) : Serializable {
        companion object {
            private val serialVersionUID: Long = 8798346516L
        }
    }
}
