package com.example.mercadolibre_ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.model.UiProduct

const val PRODUCT_DETAIL_FRAGMENT_TAG = "ProductDetailFragmentTag"
private const val PRODUCT_KEY = "PRODUCT_KEY"

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
}
