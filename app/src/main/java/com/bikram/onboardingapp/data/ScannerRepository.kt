package com.bikram.onboardingapp.data

import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Repository that fetch barcode data from google scanner module.
 */
interface ScannerRepository {
    fun startScanning(): Flow<String?>
}

/**
 * Implementation of Repository that produces barcode data.
 */
class BarcodeScannerRepository @Inject constructor(
    private val scanner: GmsBarcodeScanner
) : ScannerRepository {
    override fun startScanning(): Flow<String?> {
        return callbackFlow {
            scanner.startScan()
                .addOnSuccessListener {
                    launch {
                        send(getDetails(it))
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                }
            awaitClose { }
        }
    }

    private fun getDetails(barcode: Barcode): String {
        return if (barcode.displayValue!!.contains("SUPERMARKET-"))
            barcode.displayValue!!.split("-")[1]
        else
            "Couldn't determine"

        //Actual case
//        return when (barcode.valueType) {
//            Barcode.TYPE_PRODUCT -> {
//                "productType : ${barcode.displayValue}"
//            }
//
//            else -> {
//                "Couldn't determine"
//            }
//        }
    }
}