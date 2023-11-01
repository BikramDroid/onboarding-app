package com.bikram.onboardingapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikram.onboardingapp.data.ProductsRepository
import com.bikram.onboardingapp.data.ScannerRepository
import com.bikram.onboardingapp.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Home screen
 */
sealed interface ProductsUiState {
    data class Success(val products: List<Product>) : ProductsUiState
    object Error : ProductsUiState
    object Loading : ProductsUiState
}

/**
 * UI state for the scanner
 */
data class ScannerUiState(
    var barcodeDetails: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    productsRepository: ProductsRepository,
    private val scannerRepo: ScannerRepository
) :
    ViewModel() {
    val productsUiState = productsRepository.getProductsList().map {
        try {
            ProductsUiState.Success(it)
        } catch (e: Exception) {
            ProductsUiState.Error
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ProductsUiState.Loading)

    // Scanner barcode area
    private val _scannerState = MutableStateFlow(ScannerUiState())
    val scannerUiState = _scannerState.asStateFlow()

    fun startScanning() {
        viewModelScope.launch {
            scannerRepo.startScanning().collect {
                if (!it.isNullOrBlank()) {
                    _scannerState.value = scannerUiState.value.copy(
                        barcodeDetails = it
                    )
                }
            }
        }
    }
}