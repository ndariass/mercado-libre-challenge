package com.example.mercadolibre_ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mercadolibre_domain.model.Product
import com.example.mercadolibre_domain.model.Response
import com.example.mercadolibre_domain.repository.ProductsRepository
import com.example.mercadolibre_ui.manager.ProductsManager
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
    private val productsManager: ProductsManager
) :
    ViewModel() {

    private val _products = MutableLiveData<List<UiProduct>>()
    private val _productDetailNavigation = MutableLiveData<UiProduct>()

    val products: LiveData<List<UiProduct>> = _products
    val productDetailNavigation = _productDetailNavigation

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    fun navigateToProductDetail(uiProduct: UiProduct) {
        _productDetailNavigation.value = uiProduct
    }

    fun searchProducts(query: String) {
        coroutineScope.launch(Dispatchers.Main) {
            val result: Response<List<Product>> = fetchProductsResult(query)

            if (result.successful && result.payload != null) {
                _products.value = buildUiProducts(result.payload!!)
            } else {
                //Handle error
                //Handle empty error
            }
        }
    }

    private suspend fun fetchProductsResult(query: String) =
        withContext(Dispatchers.IO) {
            productsRepository.searchProducts(query)
        }

    private suspend fun buildUiProducts(products: List<Product>) =
        withContext(Dispatchers.Default) {
            products.map(productsManager::buildUiProduct)
        }

    override fun onCleared() {
        parentJob.cancel()
        super.onCleared()
    }
}
