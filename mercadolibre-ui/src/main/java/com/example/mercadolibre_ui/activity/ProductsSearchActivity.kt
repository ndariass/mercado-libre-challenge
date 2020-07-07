package com.example.mercadolibre_ui.activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mercadolibre_domain.repository.ProductsRepository
import com.example.mercadolibre_ui.R
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * [AppCompatActivity] implementation for the product search screen
 *
 * @author Nicolás Arias
 */
class ProductsSearchActivity : AppCompatActivity() {

    @Inject
    lateinit var productsRepository: ProductsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_search)

        runTask()
    }

    private fun runTask() {
        object : AsyncTask<Unit, Unit, Unit>() {
            override fun onProgressUpdate(vararg values: Unit?) {

            }

            override fun doInBackground(vararg p0: Unit?) {
                val result = productsRepository.searchProducts("Huawei PSmart")
                Log.d(javaClass.name, "++++++")
                Log.d(javaClass.name, "++++++$result")
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
            }
        }.execute()
    }
}
