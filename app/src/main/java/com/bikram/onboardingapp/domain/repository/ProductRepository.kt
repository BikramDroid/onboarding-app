package com.bikram.onboardingapp.domain.repository

import com.bikram.onboardingapp.domain.model.Product
import com.bikram.onboardingapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<Product>>>
}