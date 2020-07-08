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
    val freeShipping: String?
) : Serializable {
    companion object {
        private val serialVersionUID: Long = 654484948L
    }
}
