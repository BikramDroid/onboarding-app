package com.bikram.onboardingapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.domain.model.Product
import com.bikram.onboardingapp.ui.components.CustomSpacer
import com.bikram.onboardingapp.ui.components.CustomToast
import com.bikram.onboardingapp.ui.viewmodels.ProductState

@Composable
fun ProductDetailsScreen(
    productsUiState: ProductState,
    productId: Int,
    onDismiss: () -> Unit
) {
    if (productId > productsUiState.products.size) {
        CustomToast(stringResource(id = R.string.not_found), LocalContext.current)
        onDismiss()
    } else {
        DetailsScreenContent(
            product = productsUiState.products[productId - 1],
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DetailsScreenContent(
    product: Product,
    modifier: Modifier = Modifier
) {
    Column {
        Box(
            modifier = modifier
                .weight(1f)
                .padding(top = 60.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(product.image)
                    .crossfade(true)
                    .build(),
                modifier = modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.Center),
                error = painterResource(R.drawable.loading_img),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.product_image),
                contentScale = ContentScale.Inside
            )
        }
        CustomSpacer()
        Card(
            modifier = modifier
                .fillMaxWidth()
                .weight(2f),
            shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
        ) {

            Box(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = product.title,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    )

                    CustomSpacer()

                    Text(
                        text = "kr ${product.priceKr}",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    CustomSpacer()

                    Text(
                        text = product.description,
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProductDetailsScreenPreview() {
    ProductDetailsScreen(ProductState(), productId = 0, onDismiss = {})
}