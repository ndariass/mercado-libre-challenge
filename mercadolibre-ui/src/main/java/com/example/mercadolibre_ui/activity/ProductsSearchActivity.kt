package com.example.mercadolibre_ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.fragment.PRODUCTS_SEARCH_FRAGMENT_TAG
import com.example.mercadolibre_ui.fragment.PRODUCT_KEY
import com.example.mercadolibre_ui.fragment.ProductsSearchFragment
import com.example.mercadolibre_ui.viewmodel.ProductsSearchViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * [AppCompatActivity] implementation for the product search screen
 *
 * @author Nicolás Arias
 */
class ProductsSearchActivity : AppCompatActivity() {

    @Inject
    lateinit var productsSearchViewModel: ProductsSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_search)

        productsSearchViewModel.init()

        if (savedInstanceState == null) addFragment()

        productsSearchViewModel.productDetailNavigation.observe(this, Observer {
            Intent(this, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_KEY, it)
                startActivity(this)
            }
        })
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.products_search_fragment_container,
                ProductsSearchFragment.newInstance(),
                PRODUCTS_SEARCH_FRAGMENT_TAG
            )
            .setCustomAnimations(
                R.anim.fragment_open_enter,
                R.anim.fragment_close_exit,
                R.anim.fragment_fade_enter,
                R.anim.fragment_fade_exit
            )
            .commit()
    }
}
