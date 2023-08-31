package com.bikram.onboardingapp.network

import com.bikram.onboardingapp.model.Product
import retrofit2.http.GET

/**
 * A public interface that exposes the [getProducts] method
 */
interface ProductsApiService {
    /**
     * Returns a [List] of [Product], called from a Coroutine.
     * The "products" endpoint is requested with the GET HTTP method
     */
    @GET("products")
    suspend fun getProducts(): List<Product>
}
