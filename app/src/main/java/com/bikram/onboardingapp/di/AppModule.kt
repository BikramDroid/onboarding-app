package com.bikram.onboardingapp.di

import android.app.Application
import android.content.Context
import com.bikram.onboardingapp.data.BarcodeScannerRepository
import com.bikram.onboardingapp.data.NetworkProductsRepository
import com.bikram.onboardingapp.data.ProductsRepository
import com.bikram.onboardingapp.data.ScannerRepository
import com.bikram.onboardingapp.network.ProductsApiService
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
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

@InstallIn(ViewModelComponent::class)
@Module
object ScannerModule {
    @ViewModelScoped
    @Provides
    fun providesContext(app: Application): Context {
        return app.applicationContext
    }

    @ViewModelScoped
    @Provides
    fun providesBarCodeOptions(): GmsBarcodeScannerOptions {
        return GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
    }

    @ViewModelScoped
    @Provides
    fun providesBarCodeScanner(
        context: Context,
        options: GmsBarcodeScannerOptions
    ): GmsBarcodeScanner {
        return GmsBarcodeScanning.getClient(context, options)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ScannerRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindScannerRepo(
        scannerRepoImpl: BarcodeScannerRepository
    ): ScannerRepository
}