package com.example.mercadolibre_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
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
 * [DiffUtil.ItemCallback] implementation for [ProductsAdapter]
 */
val DIFF_CALLBACK: DiffUtil.ItemCallback<UiProduct> = object : DiffUtil.ItemCallback<UiProduct>() {
    override fun areItemsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UiProduct, newItem: UiProduct): Boolean {
        return oldItem == newItem
    }
}

/**
 * [ListAdapter] implementation to display a list of products
 *
 * @author Nicolás Arias
 */
class ProductsAdapter : PagedListAdapter<UiProduct, ProductListItemViewHolder>(DIFF_CALLBACK) {

    /*
    var data: List<UiProduct> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
     */

    var itemClickListener: ((uiProduct: UiProduct) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_product, parent, false)

        return ProductListItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductListItemViewHolder, position: Int) {
        val item = getItem(holder.adapterPosition) ?: return

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
