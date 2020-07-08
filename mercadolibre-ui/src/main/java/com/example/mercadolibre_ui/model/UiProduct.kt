package com.example.mercadolibre_ui.model

import java.io.Serializable

/**
 * UI model for a product
 *
 * @author Nicolás Arias
 */
data class UiProduct(
    val title: String,
    val price: String,
    val condition: String?,
    val thumbnail: String?,
    val installments: String?,
    val freeShipping: String?,
    val detailOverview: String?,
    val availabilityLabel: String?,
    val address: String?,
    val attributes: List<Attribute>
) : Serializable {
    companion object {
        private val serialVersionUID: Long = 654484948L
    }

    data class Attribute(
        val name: String,
        val valueName: String
    ) : Serializable {
        companion object {
            private val serialVersionUID: Long = 4875131651L
        }
    }
}
