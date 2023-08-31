package com.bikram.onboardingapp.data

import com.bikram.onboardingapp.model.Product
import com.bikram.onboardingapp.network.ProductsApiService
import javax.inject.Inject

/**
 * Repository that fetch products list from productsApi.
 */
interface ProductsRepository {
    /** Fetches list of products from productsApi */
    suspend fun getProductsList(): List<Product>
}

/**
 * Network Implementation of Repository that products list from productsApi.
 */
class NetworkProductsRepository @Inject constructor(
    private val marsApiService: ProductsApiService
) : ProductsRepository {
    /** Fetches list of products from productsApi*/
    override suspend fun getProductsList(): List<Product> = marsApiService.getProducts()
}
