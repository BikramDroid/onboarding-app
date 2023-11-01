package com.bikram.onboardingapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikram.onboardingapp.data.ProductsRepository
import com.bikram.onboardingapp.data.ScannerRepository
import com.bikram.onboardingapp.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
    val barcodeDetails: String = ""
)

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    productsRepository: ProductsRepository,
    private val scannerRepo: ScannerRepository
) : ViewModel() {
    val productsUiState: StateFlow<ProductsUiState>

    // Search products area
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    lateinit var products: StateFlow<List<Product>>

    init {
        productsUiState = productsRepository.getProductsList().map {
            try {
                val _products = MutableStateFlow(it)

                products = searchText
                    .debounce(500L)
                    .onEach { _isSearching.update { true } }
                    .combine(_products) { text, products ->
                        if (text.isBlank()) {
                            emptyList()
                        } else {
                            //delay(500L)
                            products.filter {
                                it.doesMatchSearchQuery(text)
                            }
                        }
                    }
                    .onEach { _isSearching.update { false } }
                    .stateIn(
                        viewModelScope,
                        SharingStarted.WhileSubscribed(5000),
                        _products.value
                    )

                ProductsUiState.Success(it)
            } catch (e: Exception) {
                ProductsUiState.Error
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ProductsUiState.Loading
        )
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun clearSearchText() {
        _searchText.value = ""
    }

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

    fun clearBarcodeData() {
        _scannerState.value = ScannerUiState()
    }
}