package com.bikram.onboardingapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface ProductsUiState {
    data class Success(val products: String) : ProductsUiState
    object Error : ProductsUiState
    object Loading : ProductsUiState
}

class HomeViewModel() : ViewModel() {
    //The mutable State that stores the status of the most recent request
    var productsUiState: ProductsUiState by mutableStateOf(ProductsUiState.Loading)
        private set

    //Call getProducts() on init so that products can be displayed immediately.
    init {
        getProducts()
    }

    // Gets products information from the Products API Retrofit service and updates the UI state
    private fun getProducts() {
        viewModelScope.launch {
            productsUiState = ProductsUiState.Loading
            productsUiState = try {
                delay(1000L)

                //todo call api
                ProductsUiState.Success(
                    "Success: x products retrieved"
                )
            } catch (e: IOException) {
                ProductsUiState.Error
            }
            //todo handle other states
        }
    }
}
