package com.bikram.onboardingapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.ui.viewmodels.ProductsUiState

@Composable
fun HomeScreen(
    productsUiState: ProductsUiState, modifier: Modifier = Modifier
) {
    when (productsUiState) {
        is ProductsUiState.Loading -> InfoScreen(modifier = modifier.fillMaxSize())
        is ProductsUiState.Success -> ResultScreen(
            productsUiState.products, modifier = modifier.fillMaxWidth()
        )

        is ProductsUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

//The home screen displaying the loading message.
@Composable
fun InfoScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

//The home screen displaying error message.
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

//ResultScreen displaying the products retrieved.
@Composable
fun ResultScreen(photos: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(text = photos)
    }
}