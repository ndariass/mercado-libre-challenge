package com.example.mercadolibre_ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
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
        @JvmStatic
        fun newInstance() = ProductsSearchFragment()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ProductsSearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products_search, container, false)
    }

    private lateinit var productsAdapter: ProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.products_search_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ProductsSearchFragment.context)
            productsAdapter = ProductsAdapter(productsManager)
            adapter = productsAdapter
        }

        view.products_search_input_text.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchProducts(textView.text.toString())
                true
            } else {
                false
            }
        }

        viewModel.products.observe(viewLifecycleOwner, Observer {
            Log.d(ProductsSearchViewModel::class.java.name, "+++++ $it")
            productsAdapter.data = it
        })
    }
}
