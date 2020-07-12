package com.example.mercadolibre_ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.mercadolibre_ui.R
import com.example.mercadolibre_ui.fragment.PRODUCT_DETAIL_FRAGMENT_TAG
import com.example.mercadolibre_ui.fragment.PRODUCT_KEY
import com.example.mercadolibre_ui.fragment.ProductDetailFragment
import com.example.mercadolibre_ui.model.UiProduct

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val uiProduct = intent.getSerializableExtra(PRODUCT_KEY) as? UiProduct
        uiProduct?.let(this::addFragment) ?: run {
            Log.d(
                ProductDetailActivity::class.java.name,
                "Unable to retrieve uiProduct from intent. Finishing the activity"
            )
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addFragment(uiProduct: UiProduct) {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.product_detail_fragment_container,
                ProductDetailFragment.newInstance(uiProduct),
                PRODUCT_DETAIL_FRAGMENT_TAG
            )
            .commit()
    }
}