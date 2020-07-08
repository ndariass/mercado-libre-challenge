package com.example.mercadolibre_ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.fragment.PRODUCTS_SEARCH_FRAGMENT_TAG
import com.example.mercadolibre_ui.fragment.PRODUCT_DETAIL_FRAGMENT_TAG
import com.example.mercadolibre_ui.fragment.ProductDetailFragment
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

        //TODO: navigate to fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
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
            //TODO allow state loss?
        }
        //TODO: need to handle else?

        productsSearchViewModel.productDetailNavigation.observe(this, Observer {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ProductDetailFragment.newInstance(it),
                    PRODUCT_DETAIL_FRAGMENT_TAG
                )
                .addToBackStack("aab")
                .setCustomAnimations(
                    R.anim.fragment_open_enter,
                    R.anim.fragment_close_exit,
                    R.anim.fragment_fade_enter,
                    R.anim.fragment_fade_exit
                )
                //TODO allowing state loss?
                .commit()
        })
    }

    override fun onDestroy() {

        super.onDestroy()
    }
}
