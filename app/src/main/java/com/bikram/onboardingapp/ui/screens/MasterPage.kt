package com.bikram.onboardingapp.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import com.bikram.onboardingapp.ui.components.ARSample
import com.bikram.onboardingapp.ui.components.CustomAppBar
import com.bikram.onboardingapp.ui.components.CustomBottomBar
import com.bikram.onboardingapp.ui.components.CustomSearchBar
import com.bikram.onboardingapp.ui.components.CustomToast
import com.bikram.onboardingapp.ui.components.ErrorScreen
import com.bikram.onboardingapp.ui.components.LoadingScreen
import com.bikram.onboardingapp.ui.components.WindowSize
import com.bikram.onboardingapp.ui.viewmodels.HomeViewModel


/**
 * enum values that represent the screens in the app
 */
enum class AppScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    AllProducts(title = R.string.all_products),
    ProductDetails(title = R.string.product_details),
    ProductSearch(title = R.string.search_button),
    ProductAR(title = R.string.a_r),
    ProductDetection(title = R.string.obj_detection)
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

@OptIn(ExperimentalMaterialApi::class)
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
    val productsUiState = homeViewModel.productsState

    val isPageRefreshing by homeViewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isPageRefreshing,
        onRefresh = { homeViewModel.fetchProducts(true) }
    )

    val context = LocalContext.current

    val barcodeState by homeViewModel.scannerUiState.collectAsState()
    val barcode = barcodeState.barcodeDetails
    if (barcode.isNotEmpty()) {
        homeViewModel.clearBarcodeData()
        if (barcode == "Couldn't determine")
            CustomToast(
                stringResource(id = R.string.not_found),
                context
            )
        else {
            appBarTitle.value = ""
            navController.navigate(AppScreen.ProductDetails.name + "?productId=" + barcode)
        }
    }

    Scaffold(
        topBar = {
            if (navController.currentBackStackEntry?.destination?.route != AppScreen.ProductSearch.name
                && navController.currentBackStackEntry?.destination?.route != AppScreen.ProductAR.name
                && navController.currentBackStackEntry?.destination?.route != AppScreen.ProductDetection.name
            )
                CustomAppBar(selectedIndex, appBarTitle.value,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = {
                        navController.navigateUp()
                    },
                    searchButton = {
                        navController.navigate(AppScreen.ProductSearch.name)
                    })
        },
        content = {
            NavHost(
                navController = navController,
                startDestination = AppScreen.Start.name
            ) {
                composable(route = AppScreen.Start.name) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState)
                    ) {
                        Box {
                            when (selectedIndex.value) {
                                0 -> {
                                    if (productsUiState.isLoading)
                                        LoadingScreen()
                                    else if (productsUiState.isError)
                                        ErrorScreen()
                                    else
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
                                }

                                1 -> {
                                    ReceiptsScreen()
                                }

                                2 -> {
                                    ProfileScreen()
                                }
                            }

                            PullRefreshIndicator(
                                refreshing = isPageRefreshing,
                                state = pullRefreshState,
                                modifier = Modifier.align(Alignment.TopCenter),
                                contentColor = MaterialTheme.colorScheme.primary,
                            )
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
                    val category = it.arguments?.getString("productsCategory")

                    if (category != null)
                        AllProductsScreen(
                            productsUiState.products,
                            category
                        ) {
                            if (isExpanded)
                                onExpandedItemClick(it)
                            else {
                                appBarTitle.value = ""
                                navController.navigate(AppScreen.ProductDetails.name + "?productId=" + it)
                            }
                        }
                }

                composable(route = AppScreen.ProductDetails.name + "?productId={productId}",
                    arguments = listOf(
                        navArgument("productId") {
                            type = NavType.IntType
                            defaultValue = 1
                        }
                    )) {
                    val productId = it.arguments?.getInt("productId")

                    if (productId != null)
                        ProductDetailsScreen(productsUiState, productId,
                            onDismiss = {
                                navController.navigateUp()
                            },
                            onARView = {
                                //Launch via intent or a local file with custom UI
//                                navController.navigate(AppScreen.ProductAR.name)

                                val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
                                sceneViewerIntent.data =
                                    Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=https://ep.charpstar.net/Android/202090.glb&mode=ar_preferred&resizable=false&disable_occlusion=true")
                                sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
                                context.startActivity(sceneViewerIntent)
                            }
                        )
                }

                composable(route = AppScreen.ProductAR.name) {
                    ARSample()
                }

                composable(route = AppScreen.ProductDetection.name) {
                    ObjectDetectionScreen(productsUiState.products)
                }

                composable(route = AppScreen.ProductSearch.name) {
                    val searchText by homeViewModel.searchText.collectAsState()
                    val isSearching by homeViewModel.isSearching.collectAsState()
                    val searchedProducts = homeViewModel.searchedProducts.collectAsState().value

                    CustomSearchBar(
                        scanButton = {
                            navController.popBackStack()
                            homeViewModel.startScanning()
                        },
                        onDismiss = {
                            navController.navigateUp()
                        },
                        lensButton = {
                            navController.popBackStack()
                            navController.navigate(AppScreen.ProductDetection.name)
                        },
                        onCategoryButtonClicked = {
                            navController.navigateUp()

                            appBarTitle.value = it
                            navController.navigate(AppScreen.AllProducts.name + "?productsCategory=" + it)
                        },
                        onDetailsButtonClicked = {
                            navController.navigateUp()

                            appBarTitle.value = ""
                            navController.navigate(AppScreen.ProductDetails.name + "?productId=" + it)
                        },
                        onClearSearchText = {
                            homeViewModel.clearSearchText()
                        },
                        onSearchTextChange = {
                            homeViewModel.onSearchTextChange(it)
                        },
                        searchText, searchedProducts, isSearching, productsUiState.products
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
        val productsUiState = homeViewModel.productsState

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
            ProductDetailsScreen(productsUiState, selectedProductId, onDismiss = {}, onARView = {})
    }
}