package com.example.mercadolibre_ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.adapter.ProductAttributesAdapter
import com.example.mercadolibre_ui.extension.setTextOrHide
import com.example.mercadolibre_ui.model.UiProduct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_address
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_attributes
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_availability_installments
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_availability_label
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_availability_shipping
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_image
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_overview
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_price
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_scroll_view
import kotlinx.android.synthetic.main.fragment_product_detail.product_detail_title

const val PRODUCT_DETAIL_FRAGMENT_TAG = "ProductDetailFragmentTag"
const val PRODUCT_KEY = "PRODUCT_KEY"

/**
 * [Fragment] implementation to display the detail of a product
 *
 * @author Nicolás Arias
 */
class ProductDetailFragment : Fragment() {

    companion object {
        /**
         * Factory method to create an instance of this fragment with the given [UiProduct] as argument
         *
         * @param uiProduct the product to be displayed by this fragment
         * @return a new instance of this fragment
         */
        @JvmStatic
        fun newInstance(uiProduct: UiProduct) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PRODUCT_KEY, uiProduct)
                }
            }
    }

    private var uiProduct: UiProduct? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uiProduct = it.getSerializable(PRODUCT_KEY) as? UiProduct
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            setViews()
            product_detail_scroll_view.fullScroll(ScrollView.FOCUS_UP)
        }
    }

    private fun setViews() {
        uiProduct?.run {
            product_detail_overview.setTextOrHide(detailOverview)
            product_detail_title.text = title
            product_detail_price.text = price
            product_detail_availability_label.setTextOrHide(availabilityLabel)
            product_detail_availability_installments.setTextOrHide(installments)
            product_detail_availability_shipping.setTextOrHide(freeShipping)
            product_detail_address.setTextOrHide(address)

            Picasso.get()
                .load(thumbnail)
                .placeholder(R.drawable.placeholder)
                .into(product_detail_image)

            product_detail_attributes.apply {
                adapter = ProductAttributesAdapter().apply { data = attributes }
                isNestedScrollingEnabled = false
            }
        }
    }
}
