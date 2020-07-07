package com.example.mercadolibre_ui.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.manager.ProductsManager
import kotlinx.android.synthetic.main.item_list_product.view.item_list_condition
import kotlinx.android.synthetic.main.item_list_product.view.item_list_free_shipping
import kotlinx.android.synthetic.main.item_list_product.view.item_list_installments
import kotlinx.android.synthetic.main.item_list_product.view.item_list_price
import kotlinx.android.synthetic.main.item_list_product.view.item_list_title

/**
 * [RecyclerView.Adapter] implementation to display a list of products
 *
 * @author Nicolás Arias
 */
class ProductsAdapter(productsManager: ProductsManager) :
    RecyclerView.Adapter<ProductListItemViewHolder>() {

    var data: List<Product> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_product, parent, false)

        return ProductListItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ProductListItemViewHolder, position: Int) {
        val item = data[holder.adapterPosition]

        //TODO use manager
        holder.itemView.apply {
            item_list_title.text = item.title
            item_list_price.text = item.price.toString()
            item_list_installments.text = "${item.installments?.quantity?.toString()
                .orEmpty()}x ${item.installments?.amount?.toString().orEmpty()}"

            if (item.shipping.freeShipping) {
                item_list_free_shipping.text = "Envío gratis"
            } else {
                item_list_free_shipping.visibility = GONE
            }

            item_list_condition.text = item.condition?.value.orEmpty()

        }

    }
}
