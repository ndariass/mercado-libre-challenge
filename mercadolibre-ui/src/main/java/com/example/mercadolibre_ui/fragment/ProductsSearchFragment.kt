package com.example.mercadolibre_ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.adapter.ProductsAdapter
import com.example.mercadolibre_ui.util.hideKeyboard
import com.example.mercadolibre_ui.util.showKeyboard
import com.example.mercadolibre_ui.viewmodel.ProductsSearchViewModel
import com.example.mercadolibre_ui.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_products_search.products_search_error
import kotlinx.android.synthetic.main.fragment_products_search.products_search_input_layout
import kotlinx.android.synthetic.main.fragment_products_search.products_search_input_text
import kotlinx.android.synthetic.main.fragment_products_search.products_search_loader
import kotlinx.android.synthetic.main.fragment_products_search.products_search_recycler_view
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

        setUpRecyclerView()
        setUpInputLayout(view)
        listenErrors()
    }

    private fun setUpRecyclerView() {
        products_search_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            productsAdapter = ProductsAdapter()
            adapter = productsAdapter
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                .apply(this::addItemDecoration)
        }

        productsAdapter.itemClickListener = viewModel::navigateToProductDetail
    }

    private fun setUpInputLayout(view: View) {
        products_search_input_layout.setEndIconOnClickListener {
            products_search_input_text.setText("")
            products_search_input_text.requestFocus()
            showKeyboard(products_search_input_text)
        }

        products_search_input_text.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(view)
                products_search_input_text.clearFocus()

                val query = textView.text.toString()

                if (query.isBlank()) {
                    false
                } else {
                    runSearch(query)
                    true
                }
            } else {
                false
            }
        }
    }

    private fun runSearch(query: String) {
        products_search_loader.visibility = VISIBLE
        products_search_recycler_view.visibility = GONE
        products_search_error.visibility = GONE
        productsAdapter.submitList(null)

        viewModel.searchProducts(query).observe(viewLifecycleOwner, Observer {
            if (!it.isEmpty()) {
                products_search_recycler_view.post { productsAdapter.submitList(it) }
                products_search_error.visibility = GONE
                products_search_loader.visibility = GONE
                products_search_recycler_view.visibility = VISIBLE
            }
        })
    }

    private fun listenErrors() {
        viewModel.initialLoadError.observe(viewLifecycleOwner, Observer {
            products_search_error.text = it
            products_search_error.visibility = VISIBLE
            products_search_loader.visibility = GONE
            products_search_recycler_view.visibility = GONE
        })

        viewModel.rangeLoadError.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })
    }
}
