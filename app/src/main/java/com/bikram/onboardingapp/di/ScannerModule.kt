package com.bikram.onboardingapp.di

import android.app.Application
import android.content.Context
import com.bikram.onboardingapp.data.repository.ScannerRepositoryImpl
import com.bikram.onboardingapp.domain.repository.ScannerRepository
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ScannerModule {
    @Provides
    @ViewModelScoped
    fun providesContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @ViewModelScoped
    fun providesBarCodeOptions(): GmsBarcodeScannerOptions {
        return GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
    }

    @Provides
    @ViewModelScoped
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
        scannerRepoImpl: ScannerRepositoryImpl
    ): ScannerRepository
}