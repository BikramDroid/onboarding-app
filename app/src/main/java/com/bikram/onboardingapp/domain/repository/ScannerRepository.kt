package com.bikram.onboardingapp.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository that fetch barcode data from google scanner module.
 */
interface ScannerRepository {
    fun startScanning(): Flow<String?>
}