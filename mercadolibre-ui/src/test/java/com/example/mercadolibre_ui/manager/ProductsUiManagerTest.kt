package com.example.mercadolibre_ui.manager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.mercadolibre_domain.model.Error
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.extension.formatNoDecimals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Unit tests for [ProductsUiManager]
 *
 * @author Nicolás Arias
 */
@RunWith(RobolectricTestRunner::class)
class ProductsUiManagerTest {

    private lateinit var subject: ProductsUiManager

    private lateinit var context: Context

    private val productWithoutFields = buildTestProduct()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        subject = ProductsUiManager(context)
    }

    @Test
    fun test_getInitialLoadError() {
        assertEquals(
            context.getString(R.string.products_search_error_not_found),
            subject.getInitialLoadError(Error.NOT_FOUND)
        )

        assertEquals(
            context.getString(R.string.products_search_error_network),
            subject.getInitialLoadError(Error.NETWORK_ERROR)
        )

        assertEquals(
            context.getString(R.string.products_search_error_general),
            subject.getInitialLoadError(Error.GENERAL_ERROR)
        )

        assertEquals(
            context.getString(R.string.products_search_error_general),
            subject.getInitialLoadError(null)
        )
    }

    @Test
    fun test_getRangeLoadError() {
        assertEquals(
            context.getString(R.string.products_range_load_error_network),
            subject.getRangeLoadError(Error.NETWORK_ERROR)
        )

        assertEquals(
            context.getString(R.string.products_range_load_error_general),
            subject.getRangeLoadError(Error.GENERAL_ERROR)
        )

        assertNull(subject.getRangeLoadError(Error.NOT_FOUND))
    }

    @Test
    fun test_buildUiProduct() {
        val product = buildTestProduct(
            id = "id",
            title = "Title",
            price = 599000f,
            currencyId = "COP",
            availableQuantity = 2,
            soldQuantity = 1,
            condition = Product.Condition.USED,
            thumbnail = "https://thumbnail",
            installments = Product.Installments(
                quantity = 36,
                amount = 16638f,
                currencyId = "COP"
            ),
            address = "Address",
            shipping = Product.Shipping(freeShipping = true, storePickUp = true),
            attributes = listOf(
                Product.Attribute("Marca", "Motorola"),
                Product.Attribute("Procesador", "Snapdragon")
            ),
            originalPrice = 900000f
        )

        subject.buildUiProduct(product).apply {
            assertEquals(product.id, id)
            assertEquals(product.title, title)
            assertEquals("$ ${product.price!!.formatNoDecimals()}", price)
            assertEquals("Usado", condition)
            assertEquals(product.thumbnail, thumbnail)
            assertEquals("36x $ ${product.installments!!.amount.formatNoDecimals()}", installments)
            assertEquals("Envío gratis", freeShipping)
            assertEquals("1 vendido", detailOverview)
            assertEquals("Stock disponible", availabilityLabel)
            assertEquals("Address", address)
            assertEquals(product.attributes[0].name, attributes[0].name)
            assertEquals(product.attributes[0].valueName, attributes[0].valueName)
            assertEquals(product.attributes[1].name, attributes[1].name)
            assertEquals(product.attributes[1].valueName, attributes[1].valueName)
        }
    }

    @Test
    fun test_buildUiProduct_Id() {
        val productWithId = buildTestProduct(id = "id")
        val productWithoutId = buildTestProduct(
            title = "Title",
            price = 25000f,
            thumbnail = "https://thumbnail"
        )

        assertEquals("id", subject.buildUiProduct(productWithId).id)
        assertEquals(
            productWithoutId.hashCode().toString(),
            subject.buildUiProduct(productWithoutId).id
        )
    }

    @Test
    fun test_buildUiProduct_Title() {
        val productWithTitle = buildTestProduct(title = "Title")

        assertEquals("Title", subject.buildUiProduct(productWithTitle).title)
        assertEquals(
            context.getString(R.string.product_default_title),
            subject.buildUiProduct(productWithoutFields).title
        )
    }

    @Test
    fun test_buildUiProduct_Price() {
        val productWithPrice = buildTestProduct(price = 25000f)

        assertEquals(
            "$ ${productWithPrice.price!!.formatNoDecimals()}",
            subject.buildUiProduct(productWithPrice).price
        )
        assertEquals(
            context.getString(R.string.product_default_price),
            subject.buildUiProduct(productWithoutFields).price
        )
    }

    @Test
    fun test_buildUiProduct_Thumbnail() {
        assertNull(subject.buildUiProduct(buildTestProduct(thumbnail = null)).detailOverview)
        assertNull(subject.buildUiProduct(buildTestProduct(thumbnail = "  ")).detailOverview)
        assertEquals(
            "http://thumbnail",
            subject.buildUiProduct(buildTestProduct(thumbnail = "http://thumbnail")).thumbnail
        )
    }

    @Test
    fun test_buildUiProduct_DetailOverview() {
        assertEquals(
            "0 vendidos",
            subject.buildUiProduct(buildTestProduct(soldQuantity = 0)).detailOverview
        )
        assertEquals(
            "1 vendido",
            subject.buildUiProduct(buildTestProduct(soldQuantity = 1)).detailOverview
        )
        assertEquals(
            "2 vendidos",
            subject.buildUiProduct(buildTestProduct(soldQuantity = 2)).detailOverview
        )
    }

    @Test
    fun test_buildUiProduct_Condition() {
        assertEquals(
            "Usado",
            subject.buildUiProduct(buildTestProduct(condition = Product.Condition.USED)).condition
        )
        assertEquals(
            "Nuevo",
            subject.buildUiProduct(buildTestProduct(condition = Product.Condition.NEW)).condition
        )
    }

    @Test
    fun test_buildUiProduct_StockAvailability() {
        assertEquals(
            "Agotado",
            subject.buildUiProduct(buildTestProduct(availableQuantity = 0)).availabilityLabel
        )
        assertEquals(
            "Stock disponible",
            subject.buildUiProduct(buildTestProduct(availableQuantity = 1)).availabilityLabel
        )
    }

    private fun buildTestProduct(
        id: String? = null,
        title: String? = null,
        price: Float? = null,
        currencyId: String? = null,
        availableQuantity: Int? = null,
        soldQuantity: Int? = null,
        condition: Product.Condition? = null,
        thumbnail: String? = null,
        installments: Product.Installments? = null,
        address: String? = null,
        shipping: Product.Shipping = Product.Shipping(freeShipping = false, storePickUp = false),
        attributes: List<Product.Attribute> = emptyList(),
        originalPrice: Float? = null
    ): Product =
        Product(
            id = id,
            title = title,
            price = price,
            currencyId = currencyId,
            availableQuantity = availableQuantity,
            soldQuantity = soldQuantity,
            condition = condition,
            thumbnail = thumbnail,
            installments = installments,
            address = address,
            shipping = shipping,
            attributes = attributes,
            originalPrice = originalPrice
        )
}
