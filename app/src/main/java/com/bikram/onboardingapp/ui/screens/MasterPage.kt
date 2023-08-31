package com.bikram.onboardingapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bikram.onboardingapp.ui.components.CustomAppBar
import com.bikram.onboardingapp.ui.components.CustomBottomBar
import com.bikram.onboardingapp.ui.viewmodels.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun MasterPage() {
    val selectedIndex = remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            CustomAppBar(selectedIndex)
        },
        content = {
            Surface(modifier = Modifier.fillMaxSize()) {
                when (selectedIndex.value) {
                    0 -> {
                        val homeViewModel: HomeViewModel = viewModel()
                        HomeScreen(homeViewModel.productsUiState)
                    }

                    1 -> {
                        ReceiptsScreen()
                    }

                    2 -> {
                        ProfileScreen()
                    }
                }
            }
        },
        bottomBar = {
            CustomBottomBar(selectedIndex)
        }
    )
}