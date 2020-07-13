package com.example.mercadolibre_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.model.UiProduct
import kotlinx.android.synthetic.main.item_product_attribute.view.item_product_attribute_name
import kotlinx.android.synthetic.main.item_product_attribute.view.item_product_attribute_value

/**
 * [RecyclerView.Adapter] implementation to display a list of the attributes of a product
 *
 * @author Nicolás Arias
 */
class ProductAttributesAdapter : RecyclerView.Adapter<ProductAttributeItemViewHolder>() {

    var data: List<UiProduct.Attribute> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAttributeItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_attribute, parent, false)

        return ProductAttributeItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ProductAttributeItemViewHolder, position: Int) {
        val item = data[holder.adapterPosition]

        holder.itemView.apply {
            item_product_attribute_name.text = item.name
            item_product_attribute_value.text = item.valueName
        }
    }
}

/**
 * [RecyclerView.ViewHolder] implementation for a list of product attributes
 *
 * @author Nicolás Arias
 */
class ProductAttributeItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
