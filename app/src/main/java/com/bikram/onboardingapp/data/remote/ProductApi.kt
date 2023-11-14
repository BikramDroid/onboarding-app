package com.bikram.onboardingapp.data.remote

import com.bikram.onboardingapp.domain.model.Product
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): List<Product>

    companion object {
        const val BASE_URL = "https://fakestoreapi.com/"
    }
}