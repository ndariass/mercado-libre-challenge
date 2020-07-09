package com.example.mercadolibre_ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_domain.model.Response
import com.example.mercadolibre_domain.repository.ProductsRepository
import com.example.mercadolibre_ui.manager.ProductsUiManager
import com.example.mercadolibre_ui.model.UiProduct
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
class ProductsSearchViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val productsUiManager: ProductsUiManager
) :
    ViewModel() {

    private lateinit var _products: MutableLiveData<List<UiProduct>>
    private lateinit var _productDetailNavigation: MutableLiveData<UiProduct>
    private lateinit var _error: MutableLiveData<String>

    lateinit var products: LiveData<List<UiProduct>>
    lateinit var productDetailNavigation: LiveData<UiProduct>
    lateinit var error: LiveData<String>

    private var job: Job? = null

    fun init() {
        _products = MutableLiveData()
        _productDetailNavigation = MutableLiveData()
        _error = MutableLiveData()

        products = _products
        productDetailNavigation = _productDetailNavigation
        error = _error
    }

    fun navigateToProductDetail(uiProduct: UiProduct) {
        _productDetailNavigation.value = uiProduct
    }

    fun searchProducts(query: String) {
        cancelOngoingJob()

        job = Job()
        CoroutineScope(Dispatchers.Main + job!!).launch(Dispatchers.Main) {
            val result: Response<List<Product>> = fetchProductsResult(query)

            if (result.successful && result.payload != null) {
                _products.value = buildUiProducts(result.payload!!)
            } else {
                _error.value = productsUiManager.getDisplayErrorMessage(result.error)

                Log.d(
                    ProductsSearchViewModel::class.java.name,
                    result.errorMessage ?: "Unknown error when searching products"
                )
            }
        }
    }

    override fun onCleared() {
        cancelOngoingJob()
        super.onCleared()
    }

    private fun cancelOngoingJob() {
        job?.apply {
            if (!isCancelled) cancel()
        }
    }

    private suspend fun fetchProductsResult(query: String) =
        withContext(Dispatchers.IO) {
            productsRepository.searchProducts(query)
        }

    private suspend fun buildUiProducts(products: List<Product>) =
        withContext(Dispatchers.Default) {
            products.map(productsUiManager::buildUiProduct)
        }
}
