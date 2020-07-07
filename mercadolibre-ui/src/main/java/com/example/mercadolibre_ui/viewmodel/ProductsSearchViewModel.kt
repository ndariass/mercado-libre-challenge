package com.example.mercadolibre_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_domain.repository.ProductsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [ViewModel] implementation for products search
 *
 * @author Nicolás Arias
 */
@Singleton
class ProductsSearchViewModel @Inject constructor(private val productsRepository: ProductsRepository) :
    ViewModel() {

    private val _products = MutableLiveData<List<Product>>()

    val products: LiveData<List<Product>> = _products

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    fun searchProducts(query: String) {
        coroutineScope.launch(Dispatchers.Main) {
            val result = fetchProductsResult(query)


            if (result.successful && result.payload != null) {
                if (result.payload.isNullOrEmpty()) {

                } else {
                    _products.value = result.payload
                }
            } else {
                //Handle error
            }
        }
    }

    private suspend fun fetchProductsResult(query: String) =
        withContext(Dispatchers.IO) {
            productsRepository.searchProducts(query)
        }

    override fun onCleared() {
        parentJob.cancel()
        super.onCleared()
    }
}
