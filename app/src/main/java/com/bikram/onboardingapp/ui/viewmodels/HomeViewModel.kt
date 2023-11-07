package com.bikram.onboardingapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikram.onboardingapp.domain.model.Product
import com.bikram.onboardingapp.domain.repository.ProductRepository
import com.bikram.onboardingapp.domain.repository.ScannerRepository
import com.bikram.onboardingapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Home screen
 */
data class ProductState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isRefreshing: Boolean = false
)

/**
 * UI state for the scanner
 */
data class ScannerUiState(
    val barcodeDetails: String = ""
)

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    productsRepository: ProductRepository,
    private val scannerRepo: ScannerRepository
) : ViewModel() {

    // Search products area
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    lateinit var searchedProducts: StateFlow<List<Product>>

    var productsState by mutableStateOf(ProductState())

    init {
        viewModelScope.launch {
            productsRepository
                .getProducts(false, "")
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { products ->
                                productsState = productsState.copy(
                                    products = products
                                )

                                val _products = MutableStateFlow(products)
                                setupSearch(_products)
                            }
                        }

                        is Resource.Error -> {
                            productsState = productsState.copy(isLoading = false, isError = true)
                        }

                        is Resource.Loading -> {
                            productsState = productsState.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

    private fun setupSearch(_products: MutableStateFlow<List<Product>>) {
        searchedProducts = searchText
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