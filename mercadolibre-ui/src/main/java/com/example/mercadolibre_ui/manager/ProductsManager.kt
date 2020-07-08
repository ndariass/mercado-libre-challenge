package com.example.mercadolibre_ui.manager

import android.content.Context
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_ui.extension.formatNoDecimals
import com.example.mercadolibre_ui.model.UiProduct
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UI manager class for products
 *
 * @author Nicolás Arias
 */
@Singleton
class ProductsManager @Inject constructor(private val context: Context) {

    /**
     * Builds an instance of [UiProduct] from a [Product] one
     *
     * @author Nicolás Arias
     */
    fun buildUiProduct(product: Product): UiProduct =
        UiProduct(
            title = product.title,
            price = "$ ${product.price.formatNoDecimals()}",
            condition = getCondition(product.condition),
            thumbnail = product.thumbnail,
            installments = product.installments?.run { "${quantity}x $ ${amount.formatNoDecimals()}" },
            freeShipping = if (product.shipping.freeShipping) "Envío gratis" else null
        )

    private fun getCondition(condition: Product.Condition?): String? =
        when (condition) {
            Product.Condition.NEW -> "Nuevo"
            Product.Condition.USED -> "Usado"
            else -> null
        }
}
