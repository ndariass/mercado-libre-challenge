package com.example.mercadolibre_ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.adapter.ProductsAdapter
import com.example.mercadolibre_ui.manager.ProductsManager
import com.example.mercadolibre_ui.viewmodel.ProductsSearchViewModel
import com.example.mercadolibre_ui.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_products_search.view.products_search_input_text
import kotlinx.android.synthetic.main.fragment_products_search.view.products_search_recycler_view
import javax.inject.Inject

const val PRODUCTS_SEARCH_FRAGMENT_TAG = "ProductsSearchFragmentTag"

/**
 * [Fragment] implementation for the products search screen
 *
 * @author Nicolás Arias
 */
class ProductsSearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var productsManager: ProductsManager

    private lateinit var viewModel: ProductsSearchViewModel

    companion object {
        /**
         * Factory method to create a new instance of this fragment
         *
         * @return a new instance of this fragment
         */
        @JvmStatic
        fun newInstance() = ProductsSearchFragment()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ProductsSearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_products_search, container, false)

    private lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.products_search_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            productsAdapter = ProductsAdapter()
            adapter = productsAdapter
        }

        //TODO: handle input focus
        //TODO: avoid reloading on rotate. Why is this happening
        view.products_search_input_text.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchProducts(textView.text.toString())
                true
            } else {
                false
            }
        }

        productsAdapter.itemClickListener = {
            viewModel.navigateToProductDetail(it)
        }

        viewModel.products.observe(viewLifecycleOwner, Observer {
            view.products_search_recycler_view.layoutManager?.scrollToPosition(0)
            productsAdapter.data = it
        })
    }
}
