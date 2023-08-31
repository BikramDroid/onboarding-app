package com.bikram.onboardingapp.di

import com.bikram.onboardingapp.data.NetworkProductsRepository
import com.bikram.onboardingapp.data.ProductsRepository
import com.bikram.onboardingapp.network.ProductsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val baseUrl = "https://fakestoreapi.com/"

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json {
            isLenient = true
            ignoreUnknownKeys = true
        }.asConverterFactory(("application/json".toMediaType())))
        .baseUrl(baseUrl)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: ProductsApiService by lazy {
        retrofit.create(ProductsApiService::class.java)
    }

    @Provides
    fun providesProductsService(): ProductsRepository =
        NetworkProductsRepository(retrofitService)
}