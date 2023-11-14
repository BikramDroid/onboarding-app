package com.bikram.onboardingapp.di

import android.app.Application
import androidx.room.Room
import com.bikram.onboardingapp.data.local.ProductDatabase
import com.bikram.onboardingapp.data.remote.ProductApi
import com.bikram.onboardingapp.data.repository.ProductRepositoryImpl
import com.bikram.onboardingapp.domain.repository.ProductRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesProductApi(): ProductApi {
        return Retrofit.Builder()
            .baseUrl(ProductApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory(("application/json".toMediaType())))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providesProductDatabase(app: Application): ProductDatabase {
        return Room.databaseBuilder(
            app,
            ProductDatabase::class.java,
            "ProductDB.db"
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
}

