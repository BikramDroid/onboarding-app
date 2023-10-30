package com.bikram.onboardingapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.bikram.onboardingapp.ui.components.CustomToast
import com.bikram.onboardingapp.ui.components.WindowSize
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

@Composable
fun MasterPage(
    navController: NavHostController = rememberNavController(),
    windowSize: WindowSize
) {
    when (windowSize) {
        is WindowSize.Expanded -> MasterPageExpanded(windowSize, navController)
        else -> MasterPageRegular(navController)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun MasterPageRegular(
    navController: NavHostController,
    onExpandedItemClick: (Int) -> Unit = {},
    isExpanded: Boolean = false
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Get the name of the current screen
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route?.split("?")?.get(0) ?: AppScreen.Start.name
    )

    val selectedIndex = rememberSaveable() { mutableStateOf(0) }
    val appBarTitle = remember { mutableStateOf("") }

    val homeViewModel: HomeViewModel = hiltViewModel()
    val barcodeState by homeViewModel.scannerUiState.collectAsState()

    val barcode = barcodeState.barcodeDetails
    if (barcode.isNotEmpty()) {
        barcodeState.barcodeDetails = ""
        if (barcode == "Couldn't determine")
            CustomToast(
                stringResource(id = R.string.not_found),
                LocalContext.current
            )
        else {
            appBarTitle.value = ""
            navController.navigate(AppScreen.ProductDetails.name + "?productId=" + barcode)
        }
    }

    Scaffold(
        topBar = {
            CustomAppBar(selectedIndex, appBarTitle.value,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    navController.navigateUp()
                },
                searchButton = {
                    homeViewModel.startScanning()
                })
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
                                        onDetailsButtonClicked = {
                                            if (isExpanded)
                                                onExpandedItemClick(it)
                                            else {
                                                appBarTitle.value = ""
                                                navController.navigate(AppScreen.ProductDetails.name + "?productId=" + it)
                                            }
                                        }
                                    )
                                } else {
                                    HomeScreen(
                                        ProductsUiState.Error,
                                        onMoreButtonClicked = { },
                                        onDetailsButtonClicked = { })
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

                    if (category != null)
                        AllProductsScreen(
                            productsUiState,
                            category,
                            onDetailsButtonClicked = {
                                if (isExpanded)
                                    onExpandedItemClick(it)
                                else {
                                    appBarTitle.value = ""
                                    navController.navigate(AppScreen.ProductDetails.name + "?productId=" + it)
                                }
                            })
                }

                composable(route = AppScreen.ProductDetails.name + "?productId={productId}",
                    arguments = listOf(
                        navArgument("productId") {
                            type = NavType.IntType
                            defaultValue = 1
                        }
                    )) {
                    val productsUiState by homeViewModel.productsUiState.collectAsState()
                    val productId = it.arguments?.getInt("productId")

                    if (productId != null)
                        ProductDetailsScreen(productsUiState, productId,
                            onDismiss = {
                                navController.navigateUp()
                            }
                        )
                }
            }
        },
        bottomBar = {
            if (navController.previousBackStackEntry == null)
                CustomBottomBar(selectedIndex)
        }
    )
}

@Composable
private fun MasterPageExpanded(
    windowSize: WindowSize,
    navController: NavHostController
) {
    val hingeHalfSize = 15.dp
    val rowItemWidth = windowSize.size.width / 2 - hingeHalfSize
    var selectedProductId by remember { mutableStateOf<Int>(0) }

    Row(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.width(rowItemWidth)) {
            MasterPageRegular(
                navController,
                onExpandedItemClick = { selectedProductId = it },
                isExpanded = true
            )
        }

        Spacer(modifier = Modifier.width(hingeHalfSize * 2))

        val homeViewModel: HomeViewModel = hiltViewModel()
        val productsUiState by homeViewModel.productsUiState.collectAsState()

        if (selectedProductId == 0)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.details_fold),
                    modifier = Modifier.padding(top = 80.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        else
            ProductDetailsScreen(productsUiState, selectedProductId, onDismiss = {})
    }
}

@Suppress("DEPRECATION")
@Composable
fun verifyAvailableNetwork(): Boolean {
    val connectivityManager =
        LocalContext.current.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}