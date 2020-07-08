package com.example.mercadolibre_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.extension.setTextOrHide
import com.example.mercadolibre_ui.model.UiProduct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_product.view.item_list_condition
import kotlinx.android.synthetic.main.item_list_product.view.item_list_free_shipping
import kotlinx.android.synthetic.main.item_list_product.view.item_list_installments
import kotlinx.android.synthetic.main.item_list_product.view.item_list_price
import kotlinx.android.synthetic.main.item_list_product.view.item_list_thumbnail
import kotlinx.android.synthetic.main.item_list_product.view.item_list_title

/**
 * [RecyclerView.Adapter] implementation to display a list of products
 *
 * @author Nicolás Arias
 */
class ProductsAdapter : RecyclerView.Adapter<ProductListItemViewHolder>() {

    var data: List<UiProduct> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: ((uiProduct: UiProduct) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_product, parent, false)

        return ProductListItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ProductListItemViewHolder, position: Int) {
        val item = data[holder.adapterPosition]

        holder.itemView.apply {
            item_list_title.text = item.title
            item_list_price.text = item.price
            item_list_condition.setTextOrHide(item.condition)
            item_list_installments.setTextOrHide(item.installments)
            item_list_free_shipping.setTextOrHide(item.freeShipping)

            Picasso.get()
                .load(item.thumbnail)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(item_list_thumbnail)

            setOnClickListener {
                itemClickListener?.invoke(item)
            }
        }
    }
}

/**
 * [RecyclerView.ViewHolder] implementation for a product list item
 *
 * @author Nicolás Arias
 */
class ProductListItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
