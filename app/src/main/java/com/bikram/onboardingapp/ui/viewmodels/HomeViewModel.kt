package com.bikram.onboardingapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikram.onboardingapp.data.ProductsRepository
import com.bikram.onboardingapp.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * UI state for the Home screen
 */
sealed interface ProductsUiState {
    data class Success(val products: List<Product>) : ProductsUiState
    object Error : ProductsUiState
    object Loading : ProductsUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(productsRepository: ProductsRepository) :
    ViewModel() {
    val productsUiState = productsRepository.getProductsList().map {
        try {
            ProductsUiState.Success(it)
        } catch (e: Exception) {
            ProductsUiState.Error
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ProductsUiState.Loading)
}