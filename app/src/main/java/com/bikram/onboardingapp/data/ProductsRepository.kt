package com.bikram.onboardingapp.data

import com.bikram.onboardingapp.model.Product
import com.bikram.onboardingapp.network.ProductsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Repository that fetch products list from productsApi.
 */
interface ProductsRepository {
    /** Fetches list of products from productsApi */
    fun getProductsList(): Flow<List<Product>>
}

/**
 * Network Implementation of Repository that products list from productsApi.
 */
class NetworkProductsRepository @Inject constructor(
    private val productsApiService: ProductsApiService
) : ProductsRepository {
    /** Fetches list of products from productsApi*/
    override fun getProductsList() = flow {
        emit(productsApiService.getProducts())
    }
}