package com.bikram.onboardingapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.bikram.onboardingapp.domain.model.Product
import com.bikram.onboardingapp.ui.components.CameraView
import com.bikram.onboardingapp.ui.components.CustomToast
import com.bikram.onboardingapp.ui.components.ObjectAnalyzer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.label.ImageLabel

var isCardVisible = false

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ObjectDetectionScreen(productsUiState: List<Product>) {
    val context = LocalContext.current
    val cameraPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    PermissionRequired(
        permissionState = cameraPermissionState,
        permissionNotGrantedContent = {
            LaunchedEffect(Unit) {
                cameraPermissionState.launchPermissionRequest()
            }
        },
        permissionNotAvailableContent = {
            Column {
                CustomToast("Permission denied.", context)
            }
        }) {
        ScanSurface(productsUiState)
    }
}

@Composable
fun ScanSurface(productsUiState: List<Product>) {
    val imageLabels = remember { mutableStateListOf<ImageLabel>() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = Modifier.fillMaxSize()) {
        CameraView(
            context = context,
            lifecycleOwner = lifecycleOwner,
            analyzer = ObjectAnalyzer {
                //Only updating the labels when results card is not visible
                //remove the if check if you want real time, this also serves as benchmarking :D
                if (imageLabels.isEmpty() || !isCardVisible) {
                    imageLabels.clear()
                    imageLabels.addAll(it)
                }
            }
        )

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight()
        ) {
            val filteredProducts: ArrayList<Product> = ArrayList()
            imageLabels.forEach { it ->
                val items = productsUiState.filter { product ->
                    product.doesMatchSearchCustomQuery(it.text)
                }

                filteredProducts.addAll(items.toList())
            }

            if (filteredProducts.distinct().isNotEmpty())
                BottomSheet(filteredProducts.distinct())

            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
            ) {
                Text(
                    text = imageLabels.joinToString(separator = "\n") {
                        it.text + " - " + String.format(
                            "%.2f",
                            it.confidence
                        ).toFloat()
                    },
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    filteredProducts: List<Product>
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            isCardVisible = false
        },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        if (filteredProducts.isNotEmpty()) {
            LazyColumn {
                items(filteredProducts) {
                    isCardVisible = true

                    ProductItem(
                        it, onDetailsButtonClicked = {}, modifier = Modifier
                            .fillMaxWidth()
                            .height(135.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}