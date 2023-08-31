package com.bikram.onboardingapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikram.onboardingapp.data.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * UI state for the Home screen
 */
sealed interface ProductsUiState {
    data class Success(val products: String) : ProductsUiState
    object Error : ProductsUiState
    object Loading : ProductsUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(private val productsRepository: ProductsRepository) :
    ViewModel() {
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
                val listResult = productsRepository.getProductsList()
                ProductsUiState.Success(
                    "Success: ${listResult.size} products retrieved"
                )
            } catch (e: IOException) {
                ProductsUiState.Error
            } catch (e: HttpException) {
                ProductsUiState.Error
            }
        }
    }
}
