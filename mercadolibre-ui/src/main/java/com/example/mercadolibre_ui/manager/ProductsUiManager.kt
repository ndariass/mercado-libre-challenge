package com.example.mercadolibre_ui.manager

import android.content.Context
import com.example.mercadolibre_domain.model.Error
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_ui.R
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
class ProductsUiManager @Inject constructor(private val context: Context) {

    /**
     * Returns the error message to be displayed to the user when there is an error at the first
     * load of product search results
     *
     * @param error the [Error] instance returned by the repository
     * @return the message to be displayed to the user
     */
    fun getInitialLoadError(error: Error?): String =
        when (error) {
            Error.NOT_FOUND -> context.getString(R.string.products_search_error_not_found)
            Error.NETWORK_ERROR -> context.getString(R.string.products_search_error_network)
            else -> context.getString(R.string.products_search_error_general)
        }

    /**
     * Returns the error message to be displayed to the user when there is an error when loading a
     * page different to the first one when loading product search results
     *
     * @param error the [Error] instance returned by the repository
     * @return the message to be displayed to the user or null if no message is to be displayed
     */
    fun getRangeLoadError(error: Error?): String? =
        when (error) {
            Error.NETWORK_ERROR -> context.getString(R.string.products_range_load_error_network)
            Error.NOT_FOUND -> null
            else -> context.getString(R.string.products_range_load_error_general)
        }

    /**
     * Builds an instance of [UiProduct] from a [Product] one
     *
     * @author Nicolás Arias
     */
    fun buildUiProduct(product: Product): UiProduct =
        UiProduct(
            id = product.id,
            title = product.title,
            price = getPrice(product),
            condition = getCondition(product.condition),
            thumbnail = getThumbnail(product),
            installments = getInstallments(product),
            freeShipping = getFreeShipping(product),
            detailOverview = getDetailOverview(product),
            availabilityLabel = getStockAvailable(product),
            address = product.address,
            attributes = getAttributes(product)
        )

    private fun getPrice(product: Product) =
        context.getString(R.string.product_price, product.price.formatNoDecimals())

    private fun getCondition(condition: Product.Condition?): String? =
        when (condition) {
            Product.Condition.NEW -> context.getString(R.string.product_condition_new)
            Product.Condition.USED -> context.getString(R.string.product_condition_used)
            else -> null
        }

    private fun getThumbnail(product: Product): String? =
        if (product.thumbnail.isNullOrBlank()) null
        else product.thumbnail

    private fun getInstallments(product: Product) =
        product.installments?.run {
            context.getString(
                R.string.product_installments,
                quantity,
                amount.formatNoDecimals()
            )
        }

    private fun getFreeShipping(product: Product) =
        if (product.shipping.freeShipping) {
            context.getString(R.string.product_free_shipping)
        } else {
            null
        }

    private fun getDetailOverview(product: Product): String? = product.soldQuantity?.run {
        context.resources.getQuantityString(R.plurals.products_search_sold_items, this, this)
    }

    private fun getStockAvailable(product: Product) =
        product.availableQuantity
            ?.run {
                if (this > 0) context.getString(R.string.product_stock_available)
                else context.getString(R.string.product_stock_not_available)
            }

    private fun getAttributes(product: Product): List<UiProduct.Attribute> =
        product.attributes.map {
            UiProduct.Attribute(name = it.name, valueName = it.valueName)
        }
}
