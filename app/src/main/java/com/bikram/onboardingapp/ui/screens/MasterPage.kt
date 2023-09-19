package com.bikram.onboardingapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.ui.components.CustomAppBar
import com.bikram.onboardingapp.ui.components.CustomBottomBar
import com.bikram.onboardingapp.ui.viewmodels.HomeViewModel
import com.bikram.onboardingapp.ui.viewmodels.ProductsUiState

/**
 * enum values that represent the screens in the app
 */
enum class AppScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    AllProducts(title = R.string.all_products),
    ProductDetails(title = R.string.product_details),
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun MasterPage(navController: NavHostController = rememberNavController()) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Get the name of the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route?.split("?")?.get(0) ?: AppScreen.Start.name
    )

    val selectedIndex = remember { mutableStateOf(0) }
    val appBarTitle = remember { mutableStateOf("") }

    val homeViewModel: HomeViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            CustomAppBar(selectedIndex, appBarTitle.value,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })
        },
        content = {
            NavHost(
                navController = navController,
                startDestination = AppScreen.Start.name
            ) {
                composable(route = AppScreen.Start.name) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        when (selectedIndex.value) {
                            0 -> {
                                if (verifyAvailableNetwork()) {
                                    val productsUiState by homeViewModel.productsUiState.collectAsState()

                                    HomeScreen(
                                        productsUiState,
                                        onMoreButtonClicked = {
                                            appBarTitle.value = it
                                            navController.navigate(AppScreen.AllProducts.name + "?productsCategory=" + it)
                                        },
                                    )
                                } else {
                                    HomeScreen(ProductsUiState.Error, onMoreButtonClicked = { })
                                }
                            }

                            1 -> {
                                ReceiptsScreen()
                            }

                            2 -> {
                                ProfileScreen()
                            }
                        }
                    }
                }

                composable(route = AppScreen.AllProducts.name + "?productsCategory={productsCategory}",
                    arguments = listOf(
                        navArgument("productsCategory") {
                            type = NavType.StringType
                            defaultValue = "Default"
                        }
                    )) {
                    val productsUiState by homeViewModel.productsUiState.collectAsState()
                    val category = it.arguments?.getString("productsCategory")

                    if (category != null) {
                        AllProductsScreen(productsUiState, category)
                    }
                }

                //todo add details screen
//                composable(route = AppScreen.ProductDetails.name) {
//                    ProductDetails()
//                }
            }
        },
        bottomBar = {
            if (navController.previousBackStackEntry == null)
                CustomBottomBar(selectedIndex)
        }
    )
}

@Suppress("DEPRECATION")
@Composable
fun verifyAvailableNetwork(): Boolean {
    val connectivityManager =
        LocalContext.current.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

@Preview(showSystemUi = true)
@Composable
fun MasterScreenPreview() {
    MasterPage()
}