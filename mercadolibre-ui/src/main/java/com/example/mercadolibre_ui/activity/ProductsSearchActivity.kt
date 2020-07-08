package com.example.mercadolibre_ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mercadolibre_ui.R
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

        productsSearchViewModel.productDetailNavigation.observe(this, Observer {
            supportFragmentManager.beginTransaction()
                .replace()
        })
    }
}
